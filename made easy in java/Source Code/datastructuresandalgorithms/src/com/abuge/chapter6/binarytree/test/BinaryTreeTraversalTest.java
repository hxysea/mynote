package com.abuge.chapter6.binarytree.test;

import com.abuge.chapter6.binarytree.traversal.BinaryTreeNode;
import com.abuge.chapter6.binarytree.traversal.levelorder.LevelOrder;
import com.abuge.chapter6.binarytree.traversal.postorder.Postorder;
import com.abuge.chapter6.binarytree.traversal.preorder.Preorder;

import java.util.List;

/**
 * Created by AbuGe on 2017/1/9.
 */
public class BinaryTreeTraversalTest {
    public static void main(String[] args) {
        BinaryTreeNode binaryTreeNode = generateBinaryTree();

//        postorderTest(binaryTreeNode);

        levelorderTest(binaryTreeNode);
    }

    public static void levelorderTest(BinaryTreeNode binaryTreeNode) {
        LevelOrder levelOrder = new LevelOrder();
        List<Integer> result = levelOrder.traversal(binaryTreeNode);

        for (Integer element : result) {
            System.out.println(element);
        }
    }

    public static void postorderTest(BinaryTreeNode binaryTreeNode) {
        Postorder postorder = new Postorder();
        postorder.postorderByRecursive(binaryTreeNode);

        System.out.println("-------------------------------------");
        List<Integer> result = postorder.postorderByIternative(binaryTreeNode);
        for (Integer element : result) {
            System.out.println(element);
        }
        System.out.println("-------------------------------------");
    }

    public static void preorderTest(BinaryTreeNode binaryTreeNode) {
        Preorder preorder = new Preorder();

        preorder.preorderByRecursive(binaryTreeNode);

        System.out.println("-------------------------------------");
        List<Integer> result = preorder.preorderByIterative(binaryTreeNode);
        for (Integer element : result) {
            System.out.println(element);
        }
        System.out.println("-------------------------------------");
    }

    public static BinaryTreeNode generateBinaryTree() {
        BinaryTreeNode root = new BinaryTreeNode(1);

        BinaryTreeNode binaryTreeNode = new BinaryTreeNode(2);
        BinaryTreeNode binaryTreeNode1 = new BinaryTreeNode(3);
        BinaryTreeNode binaryTreeNode2 = new BinaryTreeNode(4);
        BinaryTreeNode binaryTreeNode3 = new BinaryTreeNode(5);
        BinaryTreeNode binaryTreeNode4 = new BinaryTreeNode(6);
        BinaryTreeNode binaryTreeNode5 = new BinaryTreeNode(7);

        root.setLeft(binaryTreeNode);
        root.setRight(binaryTreeNode1);

        binaryTreeNode.setLeft(binaryTreeNode2);
        binaryTreeNode.setRight(binaryTreeNode3);

        binaryTreeNode1.setLeft(binaryTreeNode4);
        binaryTreeNode1.setRight(binaryTreeNode5);
        return root;
    }
}
