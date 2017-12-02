class Solution {
    public void nextPermutation(int[] nums) {
        if(nums.length <= 1)
            return;
        int i = nums.length - 1;
        while(i > 0) {
            if(nums[i - 1] < nums[i]) {
                break;
            }
            i --;
        }
        
        if(i == 0) {
            Arrays.sort(nums);  // descending order => ascending order
        } else {
            int t = nums.length - 1;
            while(nums[t] <= nums[i - 1])
                t --;
            
            int temp = nums[t];
            nums[t] = nums[i - 1];
            nums[i - 1] = temp;
            
            Arrays.sort(nums, i, nums.length);
        }
        
    }
}
