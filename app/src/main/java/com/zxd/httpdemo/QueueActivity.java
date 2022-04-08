package com.zxd.httpdemo;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * author: zxd
 * created on: 2022/2/25 18:06
 * description:
 */
public class QueueActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_queue);

    }

    /**给你一个下标从 0 开始的整数数组 nums和一个整数 k。

     一开始你在下标0处。每一步，你最多可以往前跳k步，但你不能跳出数组的边界。也就是说，你可以从下标i跳到[i + 1， min(n - 1, i + k)]包含 两个端点的任意位置。

     你的目标是到达数组最后一个位置（下标为 n - 1），你的 得分为经过的所有数字之和。

     请你返回你能得到的 最大得分。

     示例 1：

     输入：nums = [1,-1,-2,4,-7,3], k = 2
     输出：7
     解释：你可以选择子序列 [1,-1,4,3] （上面加粗的数字），和为 7

     来源：力扣（LeetCode）
     链接：https://leetcode-cn.com/problems/jump-game-vi
     著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     *
     * @param nums
     * @param k
     * @return
     */
    private  int maxResult(int[] nums, int k) {

        if (nums == null || nums.length == 0 || k <= 0){
            return 0;
        }

        Queue<Integer> queue = new ArrayDeque<>();

        int[] temp = new int[nums.length];

        for (int i = 0; i < nums.length; i++) {

            int val = nums[i];
            int  addVal ;
            if (queue.peek() != null){
                addVal = val + queue.peek();
            }else {
                addVal  = val;
            }

            while (!queue.isEmpty() && addVal > queue.peek() ){
                queue.poll();
            }

            temp[i] = addVal;
            queue.add(val);

            if (i < k - 1){
                continue;
            }

            int needPop = temp[i - 1 -k];

            if (needPop == queue.peek()){
                queue.peek();
            }

        }
        return queue.peek();
    }


    /**
     * 给你一个整数数组 nums，有一个大小为k的滑动窗口从数组的最左侧移动到数组的最右侧。你只可以看到在滑动窗口内的 k个数字。滑动窗口每次只向右移动一位。
     *
     * 返回 滑动窗口中的最大值 。
     *
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/sliding-window-maximum
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     * @param nums
     * @param k
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private int[] maxSlidingWindow(int[] nums, int k) {

        int size = nums.length;

        ArrayList<Integer> tempResult = new ArrayList<>();

        ArrayDeque<Integer> queue = new ArrayDeque<>();

        for (int i = 0; i < size; i++) {

            int tempNum = nums[i];

            while (!queue.isEmpty()  && tempNum > queue.peekLast()){
                queue.pollFirst();
            }

            queue.add(tempNum);

            if (i < k - 1){
                continue;
            }

            tempResult.add(queue.peekFirst());

            int pollValue = nums[i-k+1];

            if (pollValue == queue.peekFirst()){
                queue.pollFirst();
            }

        }

        return tempResult.stream().mapToInt(Integer::valueOf).toArray();
    }


    /**
     * 给你二叉树的根节点 root ，返回其节点值的 层序遍历 。 （即逐层地，从左到右访问所有节点）。
     *
     * 输入：root = [3,9,20,null,null,15,7]
     * 输出：[[3],[9,20],[15,7]]
     *
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/binary-tree-level-order-traversal/
     */
    private List<List<Integer>> LevelOrder(TreeNode node){

        List<List<Integer>> result = new LinkedList<>();

        Queue<TreeNode> deque = new ArrayDeque<>();

        if (node != null){
            deque.offer(node);
        }

        while (!deque.isEmpty()){

            int n = deque.size();
            List<Integer> levelResult = new LinkedList<>();

            for (int i = 0; i < n; i++) {

                TreeNode pop = deque.poll();

                levelResult.add(pop.val);

                if (pop.left != null){
                    deque.add(pop.left);
                }

                if (pop.right != null){
                    deque.add(pop.right);
                }
            }
            result.add(levelResult);
        }

        return result;
    }


    /**
     * 给定一个二叉树
     *
     * struct Node {
     *   int val;
     *   Node *left;
     *   Node *right;
     *   Node *next;
     * }
     * 填充它的每个 next 指针，让这个指针指向其下一个右侧节点。如果找不到下一个右侧节点，则将 next 指针设置为 NULL。
     *
     * 初始状态下，所有next 指针都被设置为 NULL。
     *
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/populating-next-right-pointers-in-each-node-ii
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     * @param root
     * @return
     */
    private TreeNode connect(TreeNode root){

        if (root == null){
            return null;
        }

        Queue<TreeNode> nodeQueue = new ArrayDeque<>();
        nodeQueue.add(root);

        while (!nodeQueue.isEmpty()){

            int size = nodeQueue.size();

            for (int i = 0; i < size; i++) {

                TreeNode poll = nodeQueue.poll();

                if (i + 1 < size){
                    poll.next = nodeQueue.peek();
                }

                if (poll.left != null){
                    nodeQueue.add(poll.left);
                }

                if (poll.right != null){
                    nodeQueue.add(poll.right);
                }
            }

        }

        return root;
    }


    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode next;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right,TreeNode next) {
            this.val = val;
            this.left = left;
            this.right = right;
            this.next = next;
        }
    }

}
