package com.abuge.chapter6.binarytree.traversal.levelorder;

import com.abuge.chapter6.binarytree.traversal.BinaryTreeNode;

import java.util.*;

/**
 * Created by AbuGe on 2017/1/19.
 */
public class LevelOrder {
    public List<Integer> traversal(BinaryTreeNode root) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        if (null == root)
            return result;
        Queue<List<BinaryTreeNode>> queue = new ArrayDeque<List<BinaryTreeNode>>();
        ArrayList<BinaryTreeNode> level0 = new ArrayList<>();
        level0.add(root);
        queue.add(level0);
        while (!queue.isEmpty()) {
            ArrayList<BinaryTreeNode> levelNext = new ArrayList<>();
            List<BinaryTreeNode> levelCurrent = queue.poll();
            for (BinaryTreeNode node : levelCurrent) {
                result.add(node.getData());
                if (null != node.getLeft()) {
                    levelNext.add(node.getLeft());
                }
                if (null != node.getRight()) {
                    levelNext.add(node.getRight());
                }
            }
            if (!levelNext.isEmpty()) {
                queue.add(levelNext);
            }
        }
        return result;
    }
}
