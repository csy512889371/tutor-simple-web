package com.rjsoft.uums.api.util.tree.service;

import com.rjsoft.uums.api.util.tree.domain.FayTreeNode;

import java.util.List;

public interface IFayTree {
	List<FayTreeNode> getTree();
    List<FayTreeNode> getRoot();
    FayTreeNode getFayTreeNode(String nodeId);
}
