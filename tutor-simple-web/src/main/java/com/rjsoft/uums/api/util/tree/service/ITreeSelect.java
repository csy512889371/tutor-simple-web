package com.rjsoft.uums.api.util.tree.service;

import com.rjsoft.uums.api.util.tree.domain.TreeSelectNode;

import java.util.List;

public interface ITreeSelect {
	List<TreeSelectNode> getTree();
    List<TreeSelectNode> getRoot();
    TreeSelectNode getTreeSelectNode(String nodeId);
}
