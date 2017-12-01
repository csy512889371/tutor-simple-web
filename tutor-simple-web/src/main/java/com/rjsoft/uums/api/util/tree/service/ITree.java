package com.rjsoft.uums.api.util.tree.service;

import com.rjsoft.uums.api.util.tree.domain.TreeNode;

import java.util.List;

public interface ITree {
	List<TreeNode> getTree();
    List<TreeNode> getRoot();
    TreeNode getTreeNode(String nodeId);
}
