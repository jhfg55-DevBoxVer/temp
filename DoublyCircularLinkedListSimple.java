public class DoublyCircularLinkedListSimple<T> {
    private static class Node<T> {
        T value;
        Node<T> prev, next;
        Node(T value) { this.value = value; }
    }

    private final Node<T> sentinel;
    private int count;

    public DoublyCircularLinkedListSimple() {
        sentinel = new Node<>(null); // 哨兵节点
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        count = 0;
    }

    // 在第i个位置插入新元素x（0-based，插入到i前面）
    public void insertAt(int index, T value) {
        if (index < 0 || index > count) throw new IndexOutOfBoundsException();
        Node<T> pos = getNodeAt(index);
        insertBefore(pos, value);
    }

    // 删除第i个位置上的元素
    public T removeAt(int index) {
        if (index < 0 || index >= count) throw new IndexOutOfBoundsException();
        Node<T> node = getNodeAt(index);
        T val = node.value;
        removeNode(node);
        return val;
    }

    // 取第i个位置上的元素
    public T at(int index) {
        if (index < 0 || index >= count) throw new IndexOutOfBoundsException();
        return getNodeAt(index).value;
    }

    // 返回元素x第一次出现的位置号（0-based），未找到返回-1
    public int indexOf(T value) {
        int i = 0;
        for (Node<T> node = sentinel.next; node != sentinel; node = node.next, i++) {
            if ((value == null && node.value == null) || (value != null && value.equals(node.value))) return i;
        }
        return -1;
    }

    // 返回链表长度
    public int length() {
        return count;
    }

    // 输出所有元素
    public void printAll() {
        if (count == 0) {
            System.out.println("List is empty.");
            return;
        }
        Node<T> node = sentinel.next;
        for (int i = 0; i < count; i++) {
            System.out.print(node.value + (i < count - 1 ? " <-> " : ""));
            node = node.next;
        }
        System.out.println();
    }

    // 链表就地逆置
    public void reverse() {
        Node<T> node = sentinel;
        do {
            Node<T> tmp = node.next;
            node.next = node.prev;
            node.prev = tmp;
            node = tmp;
        } while (node != sentinel);
    }

    // 内部节点操作
    private void insertBefore(Node<T> pos, T value) {
        Node<T> node = new Node<>(value);
        node.prev = pos.prev;
        node.next = pos;
        pos.prev.next = node;
        pos.prev = node;
        count++;
    }

    private void removeNode(Node<T> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
        count--;
    }

    private Node<T> getNodeAt(int index) {
        if (index < 0 || index > count) throw new IndexOutOfBoundsException();
        Node<T> node;
        if (index < count / 2) {
            node = sentinel.next;
            for (int i = 0; i < index; i++) node = node.next;
        } else {
            node = sentinel;
            for (int i = count; i > index; i--) node = node.prev;
        }
        return node;
    }

    // 测试主方法
    public static void main(String[] args) {
        DoublyCircularLinkedListSimple<Integer> list = new DoublyCircularLinkedListSimple<>();
        System.out.println("1. 建立空表并输出:");
        list.printAll();

        System.out.println("2. 在第0个位置插入元素10:");
        list.insertAt(0, 10);
        list.printAll();

        System.out.println("3. 在第1个位置插入元素20:");
        list.insertAt(1, 20);
        list.printAll();

        System.out.println("4. 在第1个位置插入元素15:");
        list.insertAt(1, 15);
        list.printAll();

        System.out.println("5. 删除第1个位置的元素:");
        list.removeAt(1);
        list.printAll();

        System.out.println("6. 取第1个位置的元素:");
        System.out.println(list.at(1));

        System.out.println("7. 查找元素10第一次出现的位置:");
        System.out.println(list.indexOf(10));

        System.out.println("8. 链表长度:");
        System.out.println(list.length());

        System.out.println("9. 输出所有元素:");
        list.printAll();

        System.out.println("10. 链表逆置:");
        list.reverse();
        list.printAll();
    }
}
