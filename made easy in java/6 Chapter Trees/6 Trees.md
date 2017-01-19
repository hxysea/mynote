----------
1/1/2017 5:02:54 PM 

----------
# Trees #

1. **What** is a Tree?
	- 非线性数据结构
2.  **Glossay** 术语
	- root
	- edge
	- leaf
	- sibling
	- ancestor
	- descendant
	- level
	- depth
	- height
	- height of a tree
	- depth of a tree
	- the size of a node
3.  Binary Tree
	- Strict Binary Tree
	- Full Binary Tree
	- Complete Binary Tree
4.  Binary Tree Traversals
	- Preorder Traversal(DLR) 时间复杂度O(n)，空间复杂度O(n)
		- visit the root
		- traverse the left subtree in preorder
		- traverse the right subtree in preorder
	- Inorder Travelsals(LDR) 时间复杂度O(n)，空间复杂度O(n)
		- traverse the left subtree in inorder
		- visit the root
		- traverse the right subtree in inorder
	- Postorder Traversal(LRD) 时间复杂度O(n)，空间复杂度O(n)
		- traverse the left subtree in postorder
		- traverse the right subtree in postorder
		- visit the root
	- Level Order Traversal
		- visit the root
		- while traversing level l, keep all the elments at level + 1 in queue
		- go to the next level and visit all the nodes at the level
		- repeat this until all levels are completed
 
