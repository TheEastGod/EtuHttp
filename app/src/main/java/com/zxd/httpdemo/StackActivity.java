package com.zxd.httpdemo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Stack;

/**
 * author: zxd
 * created on: 2021/3/4 14:23
 * description:
 */
public class StackActivity extends AppCompatActivity {

    private EditText editTextOne;
    private Button buttonOne;
    private TextView textViewOne;

    private EditText editTextTwo;
    private EditText editTextThree;
    private Button buttonTwo;
    private TextView textViewTwo;

    private EditText editText4;
    private Button button3;
    private TextView textView3;


    private EditText editText5;
    private EditText editText6;
    private Button button4;
    private TextView textView4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stack);
        editTextOne = findViewById(R.id.edit_text_1);
        buttonOne = findViewById(R.id.button_1);
        textViewOne = findViewById(R.id.text_1);

        buttonOne.setOnClickListener(v -> {
            textViewOne.setText(isValid(editTextOne.getText().toString().trim()) +"");
        });

        editTextTwo = findViewById(R.id.edit_text_2);
        editTextThree = findViewById(R.id.edit_text_3);
        buttonTwo = findViewById(R.id.button_2);
        textViewTwo = findViewById(R.id.text_2);

        buttonTwo.setOnClickListener(v ->{
            textViewTwo.setText(solution()+"");
        });

        editText4 = findViewById(R.id.edit_text_4);
        button3 = findViewById(R.id.button_3);
        textView3 = findViewById(R.id.text_3);
        button3.setOnClickListener(v -> {
            textView3.setText(findRightSmall());
        });


        editText5 = findViewById(R.id.edit_text_5);
        editText6 = findViewById(R.id.edit_text_6);
        button4 = findViewById(R.id.button_4);
        textView4 = findViewById(R.id.text_4);
        button4.setOnClickListener(v -> {
            textView4.setText(findDictionary());
        });


        findViewById(R.id.button_99).setOnClickListener(v -> {
            finish();
        });
    }

    /**
     * 【题目】给定一个正整数数组和 k，要求依次取出 k 个数，输出其中数组的一个子序列，需要满足：1. 长度为 k；2.字典序最小。
     *
     * 输入：nums = [3,5,2,6], k = 2      [9, 2, 4, 5, 1, 2, 3, 0], k = 3
     * 输出：[2,6]                        [1,2,0]
     *
     * 解释：在所有可能的解：{[3,5], [3,2], [3,6], [5,2], [5,6], [2,6]} 中，[2,6] 字典序最小。
     *
     * 所谓字典序就是，给定两个数组：x = [x1,x2,x3,x4]，y = [y1,y2,y3,y4]，如果 0 ≤ p < i，xp == yp 且 xi < yi，那么我们认为 x 的字典序小于 y。
     *
     */
    private String findDictionary(){
        String s  =  editText5.getText().toString().trim();
        String[] split1 = s.split(",");

        int offset = Integer.parseInt(editText6.getText().toString().trim());

        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i < split1.length; i++) {

            int currentVal = Integer.parseInt(split1[i]);

            if (stack.size() < offset){
                stack.push(currentVal);
            }

            while (!stack.empty() && currentVal < stack.peek()  &&  stack.size() > (offset - (split1.length - i )) ){
                   stack.pop();
            }

            stack.push(currentVal);
        }

        StringBuilder stringBuffer = new StringBuilder();

        while (!stack.empty()){
            stringBuffer.append(stack.pop());
        }
        return stringBuffer.toString();
    }

    /**
     * 【题目】一个整数数组 A，找到每个元素：右边第一个比我小的下标位置，没有则用 -1 表示。
     *
     * 输入：[5, 2]     [1,2,4,9,4,0,5]
     *
     * 输出：[1, -1]    [5,5,5,4,5,-1,-1]
     * @return
     */
    private String findRightSmall(){
        String s = editText4.getText().toString().trim();
        String[] split1 = s.split(",");

        //结果数组
        int[] response = new int[split1.length];

        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i < split1.length; i++) {

            int current = Integer.parseInt(split1[i].trim());

            while (!stack.empty()){

                Integer peek = stack.peek();

                if (current >= Integer.parseInt(split1[peek].trim())){
                     stack.push(i);
                     break;
                }

                Integer pop = stack.pop();
                response[pop] = i;
            }

            stack.push(i);
        }

        while (!stack.empty()){
            Integer pop = stack.pop();
            response[pop] = -1;
        }

        StringBuilder stringBuffer = new StringBuilder();

        for (int i : response) {
            stringBuffer.append(i);
        }

        return stringBuffer.toString();
    }

    /**
     * 在水中有许多鱼，可以认为这些鱼停放在 x 轴上。再给定两个数组 Size，Dir，Size[i] 表示第 i 条鱼的大小，Dir[i]
     * 表示鱼的方向 （0 表示向左游，1 表示向右游）。这两个数组分别表示鱼的大小和游动的方向，并且两个数组的长度相等。
     * 鱼的行为符合以下几个条件:
     *
     * 所有的鱼都同时开始游动，每次按照鱼的方向，都游动一个单位距离；
     *
     * 当方向相对时，大鱼会吃掉小鱼；
     *
     * 鱼的大小都不一样。
     *
     * 输入：Size = [4, 2, 5, 3, 1], Dir = [1, 1, 0, 0, 0]
     *
     * 输出：3
     */
    private int solution(){
        String s1 = editTextTwo.getText().toString().trim();
        String s2 = editTextThree.getText().toString().trim();
        String[] split1 = s1.split(",");
        String[] split2 = s2.split(",");
        if (split1.length != split2.length){
            Toast.makeText(this,"参数错误",Toast.LENGTH_SHORT).show();
            return 0;
        }

        final int left = 0 ;

        final int right = 1;

        int size = split1.length;

        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i < size; i++) {

            boolean hasEat = false;

            int fishSize = Integer.parseInt(split1[i].trim());
            int fishDirection = Integer.parseInt(split2[i].trim());

            while (!stack.empty() &&  Integer.parseInt(split2[stack.peek()].trim()) == right && fishDirection == left) {

                if (Integer.parseInt(split1[stack.peek()].trim())  > fishSize) {
                    hasEat = true;
                    break;
                }
                stack.pop();
            }
            if (!hasEat){
                stack.push(i);
            }
        }

        return stack.size();
    }

    /**
     * 字符串中只有字符'('和')'。合法字符串需要括号可以配对。比如：
     *
     * 输入："()"
     *
     * 输出：true
     * @param s
     * @return
     */
    private boolean isValid(String s) {

        if (s == null || s.isEmpty()) {
            return true;
        }

        if (s.length() % 2 == 1) {
            return false;
        }

        Stack<Character> characters = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '{'){
                characters.push(c);
            }else if (c == '}'){
                if (characters.empty()){
                    return false;
                }
                characters.pop();
            }
        }
        return characters.empty();
    }

    /**
     * 给定一个只包含三种字符的字符串：( , )和 *，写一个函数来检验这个字符串是否为有效字符串。有效字符串具有如下规则：
     *
     * 任何左括号 (必须有相应的右括号 )。
     * 任何右括号 )必须有相应的左括号 (。
     * 左括号 ( 必须在对应的右括号之前 )。
     * *可以被视为单个右括号 )，或单个左括号 (，或一个空字符串。
     * 一个空字符串也被视为有效字符串。
     *
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/valid-parenthesis-string
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     */

    private boolean isValid2(String s){

        if (s == null || s.isEmpty()){
            return true;
        }

        Stack<Character> leftStack = new Stack<>();
        Stack<Character> starStack = new Stack<>();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '('){
                leftStack.push(c);
            }else if (c == '*'){
                starStack.push(c);
            }else if (c == ')'){
                if (leftStack.empty() && starStack.empty()){
                    return false;
                }
                if (!leftStack.empty()){
                    leftStack.pop();
                }else {
                    starStack.pop();
                }
            }
        }

        return starStack.size() >= leftStack.size();

    }


    /**
     * 给定 n 个非负整数，用来表示柱状图中各个柱子的高度。每个柱子彼此相邻，且宽度为 1 。
     *
     * 求在该柱状图中，能够勾勒出来的矩形的最大面积。
     *
     * 输入：heights = [2,1,5,6,2,3]
     * 输出：10
     * 解释：最大的矩形为图中红色区域，面积为 10
     *
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/largest-rectangle-in-histogram/description/
     */
    private int largestRectangleArea(int[] heights) {

        if (heights.length == 0){
            return 0;
        }

        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < heights.length - 1; i++) {

            int current = heights[i];
            int next  = heights[i+1];

            int min = Math.min(current, next);

            while (!stack.empty() && stack.peek() < min){
                   stack.pop();
            }

            if (stack.empty() || stack.peek() < min){
                stack.push(min);
            }
        }

        return stack.pop() *2 ;

    }


}
