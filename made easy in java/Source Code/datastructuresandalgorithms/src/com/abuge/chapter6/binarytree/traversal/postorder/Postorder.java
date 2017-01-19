package com.abuge.chapter6.binarytree.traversal.postorder;

import com.abuge.chapter6.binarytree.traversal.BinaryTreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by AbuGe on 2017/1/18.
 */
public class Postorder {
    /**
     * 递归方式实现后序遍历二叉树
     *
     * @param root
     */
    public void postorderByRecursive(BinaryTreeNode root) {
        if (null != root) {
            postorderByRecursive(root.getLeft());
            postorderByRecursive(root.getRight());
            System.out.println(root.getData());
        }
    }

    /**
     * 迭代方式实现后序遍历二叉树
     * 1.从上到下入栈左子树节点
     * 2.当前与前一个节点相同时出栈
     * 3.父子节点转换时入栈右子树
     * 4.循环1-3
     *
     * @param root
     * @return
     */
    public List<Integer> postorderByIternative(BinaryTreeNode root) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        if (null == root) {
            return result;
        }

        Stack<BinaryTreeNode> nodes = new Stack<BinaryTreeNode>();
        nodes.push(root);
        BinaryTreeNode prev = null;
        while (!nodes.isEmpty()) {
            BinaryTreeNode currentNode = nodes.peek();
            if (null == prev || prev.getLeft() == currentNode || prev.getRight() == currentNode) {
                if (null != currentNode.getLeft()) {
                    nodes.push(currentNode.getLeft());
                } else if (null != currentNode.getRight()) {//左节点为null时增加右节点判断
                    nodes.push(currentNode.getRight());
                }
            } else if (prev == currentNode.getLeft()) {
                if (null != currentNode.getRight()) {
                    nodes.push(currentNode.getRight());
                }
            } else {
                result.add(currentNode.getData());
                nodes.pop();
            }
            prev = currentNode;
        }

        return result;
    }
}
