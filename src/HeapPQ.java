public class HeapPQ {
  static TreeNode[] heap;
  static int size;
  public HeapPQ (TreeNode[] arr) {
    heap = arr;
    size = arr.length;
    sort();
  }
  static void sort() {
    int n = heap.length;
    for (int i = n / 2 - 1; i >= 0; i--)
    heapify(n, i);
    for (int i=n-1; i>=0; i--)  {
      TreeNode temp = heap[0];
      heap[0] = heap[i];
      heap[i] = temp;
      heapify(i, 0);
    }
  }
  static void heapify(int n, int i) {
    int largest = i;
    int l = 2*i + 1;
    int r = 2*i + 2;
    if (l < n && heap[l].frekans > heap[largest].frekans)
    largest = l;
    else if (l < n && heap[l].frekans == heap[largest].frekans)
      if(heap[l].c < heap[largest].c)
        largest = l;
    if (r < n && heap[r].frekans > heap[largest].frekans)
    largest = r;
    else if (r < n && heap[r].frekans == heap[largest].frekans)
      if(heap[r].c < heap[largest].c)
        largest = r;
    if (largest != i)  {
      TreeNode swap = heap[i];
      heap[i] = heap[largest];
      heap[largest] = swap;
      heapify(n, largest);
    }
  }
  static void printarray() {
    int n = heap.length;
    for (int i=0; i<n; ++i)
    if(heap[i].frekans != 2147483647)
    System.out.println(heap[i].c + " " + heap[i].frekans);
    System.out.println();
  }
  static TreeNode removeMin() {
    TreeNode min = heap[0];
    heap[0] = new TreeNode('#',2147483647);  //max value
    size--;
    sort();
    return min;
  }
  static void insert(TreeNode node) {
    heap[heap.length-1] = node;
    size++;
    sort();
  }
}
