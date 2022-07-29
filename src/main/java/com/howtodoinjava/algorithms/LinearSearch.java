class LinearSearch {
    public static void main( String args[] ) {

        //given an array of elements
        int[] arr = { 3, 4, 1, 7, 5 };

        //given search element
        int ele = 7;

        //get length of the array
        int n = arr.length;

        //call lnrSearch method and display returned result
        System.out.println(lnrSearch(arr, n, ele));
    }

    //method to check for a element using linear search
    static String lnrSearch(int arr[], int n, int ele){
      for (int i = 0; i < n; i++) {
            //checks for matching element
            if (arr[i] == ele)
                return "Given element is found at the index "+ i;
      }
      return "Given element is not found in the array.";
    }
}
