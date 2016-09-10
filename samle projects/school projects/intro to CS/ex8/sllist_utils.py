from sllist import List, Node

def merge_lists(first_list, second_list):
    """
    Merges two sorted (in ascending order) lists into one new sorted list in
    an ascending order. The resulting new list is created using new nodes
    (copies of the nodes of the given lists). Assumes both lists are sorted in
    ascending order. The original lists should not be modified.
    """
    first_l_node = first_list.head
    second_l_node = second_list.head
    new_list = List()
    while first_l_node != None or second_l_node != None:
        if first_l_node == None:
            new_list.add_first(second_l_node.get_data())
            second_l_node = second_l_node.get_next()
        elif second_l_node == None:
            new_list.add_first(first_l_node.get_data())
            first_l_node = first_l_node.get_next()
        elif first_l_node.get_data() < second_l_node.get_data():
            new_list.add_first(first_l_node.get_data())
            first_l_node = first_l_node.get_next()
        else:
            new_list.add_first(second_l_node.get_data())
            second_l_node = second_l_node.get_next()
    reverse(new_list)
    return new_list

def contains_cycle(sll):
    """
    Checks if the given list contains a cycle.
    A list contains a cycle if at some point a Node in the list points to
    a Node that already appeared in the list. Note that the cycle does not
    necessarily contain all the nodes in the list. The original list should
    not be modified.
    Returns true iff the list contains a cycle
    """
    ## I used my variation on Floyd's method of cycle detection
    
    first_node = sll.head
    if first_node == None:
        return False
    second_node = first_node.get_next()
    if second_node == None:
        return False
    
    while first_node != second_node:
        first_node = first_node.get_next()
        second_node = second_node.get_next()
        if second_node == None:
            return False
        else:
            if first_node == second_node:
                return True
            second_node = second_node.get_next()
            if second_node == None:
                return False
    return True


def reverse(sll):
    """
    Reverses the given list (so the head becomes the last element, and every
    element points to the element that was previously before it). Runs in O(n).
    No new object is created.
    """
    current_node = sll.head
    if not current_node:
        return None
    last_node = current_node
    current_node = current_node.get_next()
    sll.head = last_node
    sll.head.set_next(None)
    while current_node:
        last_node = current_node
        current_node = current_node.get_next()
        last_node.set_next(sll.head)
        sll.head = last_node
    


def is_palindrome(sll):
    """
    Checks if the given list is a palindrome. A list is a palindrome if
    for j=0...n/2 (where n is the number of elements in the list) the
    element in location j equals to the element in location n-j.
    Note that you should compare the data stored in the nodes and
    not the node objects themselves. The original list should not be modified.
    Returns true iff the list is a palindrome
    """
##      I'm not deleting it- I like it better than using O(1) space
##    
##    liset_length = 0
##    compare_list = List()
##    curr_node1 = sll.head
##    while curr_node1:
##        liset_length += 1
##        compare_list.add_first(curr_node1.get_data())
##        curr_node1=curr_node1.get_next()
##    curr_node1 = sll.head
##    curr_node2 = compare_list.head
##    for node_num in range(liset_length//2):
##        if curr_node1.get_data() != curr_node2.get_data():
##            return False
##        curr_node1=curr_node1.get_next()
##        curr_node2=curr_node2.get_next()
##    return True
    
    list_length = 0
    front_node = sll.head
    if front_node == None:
        return True
    back_node = sll.head
    
    while back_node.get_next() != None:
        back_node = back_node.get_next()
        list_length += 1
    if back_node.get_data()[0] != front_node.get_data()[0]:
        return False
    for node_run in range(1,list_length//2+1):
        back_node = front_node.get_next()
        front_node = front_node.get_next()
        for node_back_run in range(list_length - 2*node_run):
            back_node = back_node.get_next()
        if front_node.get_data()[0] != back_node.get_data()[0]:
            return False
    return True


def have_intersection(first_list, second_list):
    """
    Checks if the two given lists intersect.
    Two lists intersect if at some point they start to share nodes.
    Once two lists intersect they become one list from that point on and
    can no longer split apart. Assumes that both lists does not contain cycles.
    Note that two lists might intersect even if their lengths are not equal.
    No new object is created, and niether list is modified.
    Returns true iff the lists intersect.
    """
    ## because they don't have cycles I can know that if they intersect they
    ##both reach the same point at their end
    
    node_after = first_list.head
    if not node_after:
        return False
    node_front = first_list.head.get_next()
    while node_front:
        node_front = node_front.get_next()
        node_after = node_after.get_next()
    node_front = second_list.head
    while node_front:
        if node_front == node_after:
            return True
        node_front = node_front.get_next()
    return False
    


def get_item(sll, k):
    """
    Returns the k'th element from of the list.
    If k > list_size returns None, if k<0 returns the k element from the end.
    """
    modified = False
    if k<0:
        reverse(sll)
        k = -k -1
        modified = True
    node = sll.head
    for node_num in range(k):
        if not node:
            break
        node = node.get_next()
    if modified:
        reverse(sll)
    if node != None:
        return node.get_data()
    return None
            
def next_by(node, howmuch):
    '''
    return the node which is "howmuch" times next
    '''
    next_node = node
    for step in range(howmuch):
        if next_node == None:
            break
        next_node = next_node.get_next()
    return next_node

def slice(sll, start, stop = None, step = 1):
    """ Returns a new list after slicing the given list from start to stop
    with a step.
    Imitates the behavior of slicing regular sequences in python.
    """
    return List()
    till_end = (stop == None)
    def cut(sll, sliced_list, start, step, till_end):
        head_node = next_by(sll.head,start-2)
        node = head_node.get_next()
        sliced_list.head = node
        for stepping in range((stop-start)//step):
            node.set_next(next_by(node, step))
            node = node.get_next()
        head_node.set_next(node.get_next())
        node.set_next(None)
    def cut_2(sll, sliced_list, start, step, till_end):
        head_node = next_by(sll.head,start-2)
        node = head_node.get_next()
        sliced_list.head = node
        while node != None:
            node.set_next(next_by(node, step))
            node = node.get_next()
        head_node.set_next(node.get_next())
        node.set_next(None)
        
    sliced_list = List()
    rev = False
    if start >= 0:
        if step > 0:
            if stop != None:
                if stop >= start:
                    cut(sll, sliced_list, start, step, till_end)
                else:
                    return List()
            else:
                cut_2(sll, sliced_list, start, step, till_end)
        else:
            rev = True
            reverse(sll)
            step = -step
            if stop != None:
                if stop <= start:
                    cut(sll, sliced_list, -start, step, till_end)
                else:
                    return List()
            else:
                cut_2(sll, sliced_list, start, step, till_end)
    else:
        rev = True
        reverse(sll)
        step = -step
        

def merge_sort(sll):
    """
    Sorts the given list using the merge-sort algorithm.
    Resulting list should be sorted in ascending order. Resulting list should
    contain the same node objects it did originally, and should be stable,
    i.e., nodes with equal data should be in the same order they were in in the
    original list. You may create a constant number of new to help sorting.
    """
    
    def merge(prev_node, head_node_1, head_node_2, last):
        node = None
        node_1 = head_node_1
        node_2 = head_node_2
        if node_2 != None:
            if head_node_1.get_data() <= head_node_2.get_data():
                node = node_1
                node_1 = node_1.get_next()
            else:
                node = node_2
                node_2 = node_2.get_next()
            if prev_node == None:
                sll.head = node
            else:
                prev_node.set_next(node)
            while not (node_1 == head_node_2 and node_2 == last):
                if node_1 == head_node_2:
                    node.set_next(node_2)
                    node = node.get_next()
                    node_2 = node_2.get_next()
                elif node_2 == last:
                    node.set_next(node_1)
                    node = node.get_next()
                    node_1 = node_1.get_next()
                elif node_1.get_data() <= node_2.get_data():
                    node.set_next(node_1)
                    node = node.get_next()
                    node_1 = node_1.get_next()
                else:
                    node.set_next(node_2)
                    node = node.get_next()
                    node_2 = node_2.get_next()
            prev_node = node
            node.set_next(last)
            return prev_node
    
    length = 0
    node = sll.head
    while node != None:
        length += 1
        node = node.get_next()
    node_num = 1
    while node_num in range(length):
        prev_node = None
        node_1 = sll.head
        node_2 = next_by(node_1, node_num)
        node_2_last = next_by(node_2, node_num)
        prev_node = merge(prev_node, node_1, node_2, node_2_last)
        node_1 = node_2_last
        node_2 = next_by(node_1, node_num)
        node_2_last = next_by(node_2, node_num)
        stop = False
        while node_2 != None:
            prev_node = merge(prev_node, node_1, node_2, node_2_last)
            if stop:
                break
            if node_2_last == None:
                stop = True
            node_1 = node_2_last
            node_2 = next_by(node_1, node_num)
            node_2_last = next_by(node_2, node_num)
        node_num = node_num*2
        
