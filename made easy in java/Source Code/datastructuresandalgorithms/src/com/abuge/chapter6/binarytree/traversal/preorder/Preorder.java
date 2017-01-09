package com.abuge.chapter6.binarytree.traversal.preorder;

import com.abuge.chapter6.binarytree.traversal.BinaryTreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by AbuGe on 2017/1/9.
 */
public class Preorder {
    /**
     * 递归实现前序二叉树遍历
     *
     * @param root
     */
    public void preorderByRecursive(BinaryTreeNode root) {
        if (null != root) {
            System.out.println(root.getData());
            preorderByRecursive(root.getLeft());
            preorderByRecursive(root.getRight());
        }
    }

    //迭代方式实现二叉树前序遍历
    public List<Integer> preorderByIterative(BinaryTreeNode root) {
        List<Integer> result = new ArrayList<Integer>();

        if (null == root)
            return result;

        Stack<BinaryTreeNode> treeNodesStack = new Stack<>();
        treeNodesStack.push(root);

        while (!treeNodesStack.isEmpty()) {
            BinaryTreeNode tmp = treeNodesStack.pop();
            result.add(tmp.getData());

            if (null != tmp.getRight()) {
                treeNodesStack.push(tmp.getRight());
            }

            if (null != tmp.getLeft()) {
                treeNodesStack.push(tmp.getLeft());
            }
        }

        return result;
    }
}
