from sllist import SkipiNode as Node


class SkipiList:

    """
    This class represents a special kind of a doubly-linked list
    called a SkipiList. A SkipiList is composed of Nodes (SkipiNode from
    sllist).cIn addition to the data, each Node has one pointer to the
    next Node in the list, and another pointer to the prev-prev Node in the
    list (hence the name "skipi"). The only data members the class contains
    are the head and the tail of the list.
    """

    def __init__(self):
        """Constructs an empty SkipiList."""
        self.head = None
        self.tail = None
        

    def add_first(self, data):
        """
        Adds an item to the beginning of a list.
        data - the item to add
        """
        if self.head == None:
            self.head = Node(data, None, None)
            self.tail = self.head
        else:
            self.head = Node(data, self.head, None)
            front_node = self.head.next.next
            if front_node != None:
                front_node.skip_back = self.head

    def remove_first(self):
        """
        Removes the first Node from the list and return its data.
        Returns that data of the removed node
        """
        if self.head != None:
            ret_node = self.head
            if self.head.next != None:
                if self.head.next.next != None:
                    self.head.next.next.skip_back = None
                    self.head = self.head.next
                else:
                    self.head = self.head.next
            else:
                self.tail = None
                self.head = None
            return ret_node.data
        return None

    def add_last(self, data):
        """
        Adds an item to the end of a list.
        data - the item to add
        """
        if self.head == None:
            self.head = Node(data,None,None)
            self.tail = self.head
        else:
            cur_node = self.head
            if cur_node.next == None:
                self.tail = Node(data, None, None)
                cur_node.next = self.tail
            else:
                while cur_node.next.next != None:
                    cur_node = cur_node.next
                self.tail = Node(data, None, cur_node)
                cur_node.next.next = self.tail

    def remove_last(self):
        """
        Removes the last Node from the list and return its data.
        The data of the removed node
        """
        if self.tail != None:
            ret_node = self.tail
            if self.tail.skip_back != None:
                self.tail = self.tail.skip_back.next
                self.tail.next = None
            else:
                if self.head == self.tail:
                    self.tail = None
                    self.head = None
                else:
                    self.tail = self.head
                    self.head.next = None
            return ret_node.data
        return None

    def remove_node(self, node):
        """
        Removes a given Node from the list, and returns its data.
        Assumes the given node is in the list. Runs in O(1).
        """
        if node == self.head:
            self.remove_first()
        elif node == self.tail:
            self.remove_last()
        else:
            node.next.skip_back.next = node.next
            if node.next.next != None:
                node.next.next.skip_back = node.next.skip_back
            node.next.skip_back = node.skip_back
        return node.data

    def __getitem__(self, k):
        """
        Returns the data of the k'th item of the list.
        If k is negative return the data of k'th item from the end of the list.
        If abs(k) > length of list raise IndexError.
        """
        if k == None:
            raise IndexError
        if self.head == None:
            raise IndexError
        if k<0:
            node = self.tail
            if node == None:
                raise IndexError
            for node_step in range((-k-1)//2):
                node = node.skip_back
                if node == None:
                    raise IndexError
            if not k%2:
                node = node.skip_back
                if node == None:
                    return self.head.data
                return node.next.data
            else:
                return node.data
        node = self.head
        for node_step in range(k):
            node = node.next
            if node == None:
                raise IndexError
        return node.data
