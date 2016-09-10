/*
 * CachingFileSystem.cpp
 *
 *  Created on: 15 April 2015
 *  Authors: katyac, adirz
 */

#define FUSE_USE_VERSION 26
#define MEM_ERR "System Error - memory allocation failure"
#define USG_ERR "usage: CachingFileSystem rootdir mountdir numberOfBlocks blockSize\n"
#define ERROR 1
#define NUM_OF_ARGS 5

#include <ctype.h>
#include <dirent.h>
#include <errno.h>
#include <fcntl.h>
#include <fuse.h>
#include <libgen.h>
#include <limits.h>
#include <stdlib.h>
#include <stdio.h>
#include <string>
#include <unistd.h>
#include <sys/stat.h>
#include "Logger.cpp"
#include <cstdlib>
#include <iostream>
#include <errno.h>
#include <math.h>




using namespace std;

typedef struct{
	char * root_dir;
	char * mount_dir;
	int number_of_blocks;
	int block_size;

} cache_data;

#define DATA ((cache_data *) fuse_get_context()->private_data)
#define OK 0

static Cache * g_cache;
static Logger * g_logger;

struct fuse_operations g_caching_oper;

/**
 * set fpath to be the full path of a file or directory with relative path of path
 */
static void fullpath(char fpath[PATH_MAX], const char *path)
{

	strcpy(fpath, DATA->root_dir);

	strncat(fpath, path, PATH_MAX);
}

/**
 * return true if path is to filesystem.log
 */
static bool is_log(const char *path)
{
	char fpath[PATH_MAX];
	fullpath(fpath, path);
	if(strcmp(fpath,  g_logger->_path) == 0)
	{
		return true;
	}
	return false;
}

/** Get file attributes.
 *
 * Similar to stat().  The 'st_dev' and 'st_blksize' fields are
 * ignored.  The 'st_ino' field is ignored except if the 'use_ino'
 * mount option is given.
 */
int caching_getattr(const char *path, struct stat *statbuf){
	g_logger->write_func("getattr");

	char fpath[PATH_MAX];
	fullpath(fpath, path);
	if(is_log(path))
	{
		return -ENOENT;
	}

	if (lstat(fpath, statbuf) != 0) {
		return -errno;
	}
	return OK;
}

/**
 * Get attributes from an open file
 *
 * This method is called instead of the getattr() method if the
 * file information is available.
 *
 * Currently this is only called after the create() method if that
 * is implemented (see above).  Later it may be called for
 * invocations of fstat() too.
 *
 * Introduced in version 2.5
 */
int caching_fgetattr(const char *path, struct stat *statbuf, struct fuse_file_info *fi){
	g_logger->write_func("fgetattr");

	if(is_log(path))
	{
		return -ENOENT;
	}

	if (!strcmp(path, "/")) {
		return caching_getattr(path, statbuf);
	}

	if (fstat(fi->fh, statbuf) < 0)
	{
		return -errno;
	}

	return OK;
}

/**
 * Check file access permissions
 *
 * This will be called for the access() system call.  If the
 * 'default_permissions' mount option is given, this method is not
 * called.
 *
 * This method is not called under Linux kernel versions 2.4.x
 *
 * Introduced in version 2.5
 */
int caching_access(const char *path, int mask)
{
	g_logger->write_func("access");
	char fpath[PATH_MAX];
	fullpath(fpath, path);
	if(is_log(path))
	{
		return -ENOENT;
	}
	if (access(fpath, mask) < 0) {
		return -errno;
	}
	return OK;
}


/** File open operation
 *
 * No creation, or truncation flags (O_CREAT, O_EXCL, O_TRUNC)
 * will be passed to open().  Open should check if the operation
 * is permitted for the given flags.  Optionally open may also
 * return an arbitrary filehandle in the fuse_file_info structure,
 * which will be passed to all file operations.

 * pay attention that the max allowed path is PATH_MAX (in limits.h).
 * if the path is longer, return error.

 * Changed in version 2.2
 */
int caching_open(const char *path, struct fuse_file_info *fi){
	g_logger->write_func("open");
	if(strlen(path) > PATH_MAX || fi->flags == O_CREAT || fi->flags == O_EXCL ||
			fi->flags == O_TRUNC){
		return -errno;
	}
	char fpath[PATH_MAX];
	fullpath(fpath, path);
	if(is_log(path))
	{
		return -ENOENT;
	}
	if((fi->fh = open(fpath, fi->flags)) < 0)
	{
		return -errno;
	}
	return OK;
}

int inline read_single_block(const char *path, char *buf, size_t size, off_t offset, off_t block_offset,
		struct fuse_file_info *fi, int block_num) {

	Cache::Block * block = g_cache->find_block(block_num, path);

	if (block == NULL) {
		char * data = new char [DATA->block_size +1]; // +1?
		if (data == NULL) {
			return -errno;
		}
		char * this_path = new char [PATH_MAX];
		if (this_path == NULL) {
			return -errno;
		}
		strcpy(this_path, path);
		int edge = pread(fi->fh, data, DATA->block_size, offset);
		if (edge < 0) {
			return -errno;
		}

		block = new Cache::Block(block_num, data, this_path);

		if (block == NULL) {
			return -errno;
		}
		data[edge] = '\0';
		g_cache->add_block(block);
	}
	block->read_from_block(buf, size, block_offset);
	return OK;
}

/** Read data from an open file
 *
 * Read should return exactly the number of bytes requested except
 * on EOF or error, otherwise the rest of the data will be
 * substituted with zeroes. 
 *
 * Changed in version 2.2
 */
int caching_read(const char *path, char *buf, size_t size, off_t offset,
		struct fuse_file_info *fi){
	g_logger->write_func("read");
	if(is_log(path))
	{
		return -ENOENT;
	}

	int size_of_file = lseek(fi->fh, 0L, SEEK_END);
	if (size_of_file < 0) {
		return -errno;
	}

	if (offset >= size_of_file) {
		if(size_of_file == 0)
		{
			return 0;
		}
		return -errno;
	}

	unsigned int size_to_read = size_of_file - offset;
	if (size_to_read > size) {
		size_to_read = size;
	}

	unsigned int readen  = 0;
	int left_to_read = size_to_read;
	int block_offset = offset % DATA->block_size;
	int block_num = ceil(offset / DATA->block_size);
	while (readen < size_to_read) {
		int read_from_block = DATA->block_size - block_offset;
		if (left_to_read < DATA->block_size) {
			read_from_block = left_to_read - block_offset;
		}

		if ((read_single_block(path, &buf[readen], read_from_block, readen + offset, block_offset, fi, block_num)) < 0) {
			return -errno;
		}

		left_to_read -= read_from_block;
		readen += read_from_block;
		block_offset = 0;
		++ block_num;
	}

	return size;
}

/** Possibly flush cached data
 *
 * BIG NOTE: This is not equivalent to fsync().  It's not a
 * request to sync dirty data.
 *
 * Flush is called on each close() of a file descriptor.  So if a
 * filesystem wants to return write errors in close() and the file
 * has cached dirty data, this is a good place to write back data
 * and return any errors.  Since many applications ignore close()
 * errors this is not always useful.
 *
 * NOTE: The flush() method may be called more than once for each
 * open().  This happens if more than one file descriptor refers
 * to an opened file due to dup(), dup2() or fork() calls.  It is
 * not possible to determine if a flush is final, so each flush
 * should be treated equally.  Multiple write-flush sequences are
 * relatively rare, so this shouldn't be a problem.
 *
 * Filesystems shouldn't assume that flush will always be called
 * after some writes, or that if will be called at all.
 *
 * Changed in version 2.2
 */
int caching_flush(const char *path, struct fuse_file_info *fi)
{
	g_logger->write_func("flush");
	if(is_log(path))
	{
		return -ENOENT;
	}
	return OK;
}

/** Release an open file
 *
 * Release is called when there are no more references to an open
 * file: all file descriptors are closed and all memory mappings
 * are unmapped.
 *
 * For every open() call there will be exactly one release() call
 * with the same flags and file descriptor.  It is possible to
 * have a file opened more than once, in which case only the last
 * release will mean, that no more reads/writes will happen on the
 * file.  The return value of release is ignored.
 *
 * Changed in version 2.2
 */
int caching_release(const char *path, struct fuse_file_info *fi){
	g_logger->write_func("release");
	if(is_log(path))
	{
		return -ENOENT;
	}
	if(close(fi->fh) < 0)
	{
		return -errno;
	}
	return OK;
}

/** Open directory
 *
 * This method should check if the open operation is permitted for
 * this  directory
 *
 * Introduced in version 2.3
 */
int caching_opendir(const char *path, struct fuse_file_info *fi){
	g_logger->write_func("opendir");
	DIR *dp;
	char fpath[PATH_MAX];
	fullpath(fpath, path);
	if(is_log(path))
	{
		return -ENOENT;
	}
	dp = opendir(fpath);
	if (dp == NULL){
		return -errno;
	}

	fi->fh = (intptr_t) dp;

	return OK;
}

/** Read directory
 *
 * This supersedes the old getdir() interface.  New applications
 * should use this.
 *
 * The readdir implementation ignores the offset parameter, and
 * passes zero to the filler function's offset.  The filler
 * function will not return '1' (unless an error happens), so the
 * whole directory is read in a single readdir operation.  This
 * works just like the old getdir() method.
 *
 * Introduced in version 2.3
 */
int caching_readdir(const char *path, void *buf, fuse_fill_dir_t filler, off_t offset,
		struct fuse_file_info *fi){
	g_logger->write_func("readdir");
	if(is_log(path))
	{
		return -ENOENT;
	}
	DIR *dp;
	struct dirent *de;

	dp = (DIR *) (uintptr_t) fi->fh;
	de = readdir(dp);
	if (de == 0) {
		return -errno;
	}

	do {
		if (filler(buf, de->d_name, NULL, 0) != 0) {
			return -ENOMEM;
		}
	} while ((de = readdir(dp)) != NULL);
	return OK;
}

/** Release directory
 *
 * Introduced in version 2.3
 */
int caching_releasedir(const char *path, struct fuse_file_info *fi){
	g_logger->write_func("releasedir");
	if(is_log(path))
	{
		return -ENOENT;
	}
	if(closedir((DIR *) (uintptr_t) fi->fh) < 0)
	{
		return -errno;
	}
	return OK;
}

/** Rename a file */
int caching_rename(const char *path, const char *newpath){
	g_logger->write_func("rename");
	if(is_log(path))
	{
		return -ENOENT;
	}

	char fpath[PATH_MAX];
	fullpath(fpath, path);

	char fnewpath[PATH_MAX];
	fullpath(fnewpath, newpath);

	g_cache->rename_in_cache(path, newpath);
	if (rename(fpath, fnewpath) < 0) {
		return -errno;
	}

	return OK;
}

/**
 * Initialize filesystem
 *
 * The return value will passed in the private_data field of
 * fuse_context to all file operations and as a parameter to the
 * destroy() method.
 *
 * Introduced in version 2.3
 * Changed in version 2.6
 */
void *caching_init(struct fuse_conn_info *conn){

	g_logger->write_func("init");
	g_cache = new Cache(DATA->number_of_blocks, DATA->block_size);
	return DATA;
}


/**
 * Clean up filesystem
 *
 * Called on filesystem exit.
 *
 * Introduced in version 2.3
 */
void caching_destroy(void *userdata){
	g_logger->write_func("destroy");
	delete g_logger;
	delete [] DATA->mount_dir;
	delete [] DATA->root_dir;
	delete g_cache;
	delete DATA;
}


/**
 * Ioctl from the FUSE sepc:
 * flags will have FUSE_IOCTL_COMPAT set for 32bit ioctls in
 * 64bit environment.  The size and direction of data is
 * determined by _IOC_*() decoding of cmd.  For _IOC_NONE,
 * data will be NULL, for _IOC_WRITE data is out area, for
 * _IOC_READ in area and if both are set in/out area.  In all
 * non-NULL cases, the area is of _IOC_SIZE(cmd) bytes.
 *
 * However, in our case, this function only needs to print cache table to the log file .
 * 
 * Introduced in version 2.8
 */
int caching_ioctl (const char *, int cmd, void *arg,
		struct fuse_file_info *, unsigned int flags, void *data){
	g_logger->write_func("ioctl");
	g_logger->write_stat(g_cache);
	return 0;
}


/**
 *  Initialise the operations.
 *  You are not supposed to change this function.
 */
void init_caching_oper()
{

	g_caching_oper.getattr = caching_getattr;
	g_caching_oper.access = caching_access;
	g_caching_oper.open = caching_open;
	g_caching_oper.read = caching_read;
	g_caching_oper.flush = caching_flush;
	g_caching_oper.release = caching_release;
	g_caching_oper.opendir = caching_opendir;
	g_caching_oper.readdir = caching_readdir;
	g_caching_oper.releasedir = caching_releasedir;
	g_caching_oper.rename = caching_rename;
	g_caching_oper.init = caching_init;
	g_caching_oper.destroy = caching_destroy;
	g_caching_oper.ioctl = caching_ioctl;
	g_caching_oper.fgetattr = caching_fgetattr;

	g_caching_oper.readlink = NULL;
	g_caching_oper.getdir = NULL;
	g_caching_oper.mknod = NULL;
	g_caching_oper.mkdir = NULL;
	g_caching_oper.unlink = NULL;
	g_caching_oper.rmdir = NULL;
	g_caching_oper.symlink = NULL;
	g_caching_oper.link = NULL;
	g_caching_oper.chmod = NULL;
	g_caching_oper.chown = NULL;
	g_caching_oper.truncate = NULL;
	g_caching_oper.utime = NULL;
	g_caching_oper.write = NULL;
	g_caching_oper.statfs = NULL;
	g_caching_oper.fsync = NULL;
	g_caching_oper.setxattr = NULL;
	g_caching_oper.getxattr = NULL;
	g_caching_oper.listxattr = NULL;
	g_caching_oper.removexattr = NULL;
	g_caching_oper.fsyncdir = NULL;
	g_caching_oper.create = NULL;
	g_caching_oper.ftruncate = NULL;
}


/**
 * $ CachingFileSystem rootdir mountdir numberOfBlocks blockSize
 *
 * argv[1] = rootdir
 * rootdir- a directory (folder) containing files, which will be mounted by your filesystem at mountdir.
 * This means that when using your file system, mountdir should respond as if it contains all the
 * files in rootdir, and rootdir is where they really are.
 * Thus the user should be able to open and read the files in rootdir
 * (through your filesystem of course) using a path through mountdir.
 * All the actions done on a file in mountdir (again, using your file system)
 *
 * argv[2] = mountdir
 * mountdir - the mount point directory which should be empty.
 * The files in your file system will appear to be in this directory.
 *
 * argv[3] = numberOfBlocks
 * argv[4] = blockSize
 */
int main(int argc, char* argv[]){

	if(argc != NUM_OF_ARGS)
	{
		cout << USG_ERR;
		return ERROR;
	}
	cache_data * data = new cache_data();
	if(NULL == data)
	{
		cerr << MEM_ERR;
		return -errno;
	}

	data->root_dir = new char[PATH_MAX];
	if(NULL == data->root_dir)
	{
		cerr << MEM_ERR;
		delete data;
		return -errno;
	}
	strcpy(data->root_dir, argv[1]);
	data->mount_dir = new char[PATH_MAX];
	if(NULL == data->mount_dir)
	{
		cerr << MEM_ERR;
		delete data->root_dir;
		delete data;
		return -errno;
	}
	strcpy(data->mount_dir, argv[2]);
	data->number_of_blocks = atoi(argv[3]);
	data->block_size = atoi(argv[4]);
	bool err = false;
	if(data->number_of_blocks <= 0 || data->block_size <= 0)
	{
		err = true;
	}

	struct stat info;
	for(int i = 1; i < 3; i ++)
	{
		if( err || ( stat(argv[i], &info) != 0 && !S_ISDIR(info.st_mode)) ){
			delete data->root_dir;
			delete data->mount_dir;
			delete data;
			cout << USG_ERR;
			return ERROR;
		}
	}

	g_logger = new Logger(data->root_dir);
	init_caching_oper();
	argv[1] = argv[2];
	for (int i = 2; i< (argc - 1); i++){
		argv[i] = NULL;
	}

	argv[2] = (char*) "-s";
	argc = 3;

	int fuse_stat = fuse_main(argc, argv, &g_caching_oper, data);
	return fuse_stat;
}
