package com.abuge.chapter6.binarytree.traversal.inorder;

import com.abuge.chapter6.binarytree.traversal.BinaryTreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by AbuGe on 2017/1/10.
 */
public class Inorder {
    /**
     * 递归实现中序遍历二叉树
     *
     * @param root
     */
    public void inorderByRecursive(BinaryTreeNode root) {
        if (null != root) {
            inorderByRecursive(root.getLeft());
            System.out.println(root.getData());
            inorderByRecursive(root.getRight());
        }
    }

    /**
     * 迭代实现中序遍历二叉树
     *
     * @param root
     * @return
     */
    public List<Integer> inorderByIterative(BinaryTreeNode root) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Stack<BinaryTreeNode> binaryTreeNodes = new Stack<BinaryTreeNode>();
        BinaryTreeNode currentNode = root;
        boolean isdone = false;

        while (!isdone) {
            if (null != currentNode) {
                binaryTreeNodes.push(currentNode);
                currentNode = currentNode.getLeft();
            } else {
                if (binaryTreeNodes.isEmpty()) {
                    isdone = true;
                } else {
                    currentNode = binaryTreeNodes.pop();
                    result.add(currentNode.getData());
                    currentNode = currentNode.getRight();
                }
            }
        }

        return result;
    }

}
