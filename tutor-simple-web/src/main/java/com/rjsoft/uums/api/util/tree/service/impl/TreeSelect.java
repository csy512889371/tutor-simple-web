package com.rjsoft.uums.api.util.tree.service.impl;

import com.rjsoft.uums.api.util.tree.domain.TreeSelectNode;
import com.rjsoft.uums.api.util.tree.service.ITreeNode;
import com.rjsoft.uums.api.util.tree.service.ITreeSelect;

import java.util.*;

public class TreeSelect implements ITreeSelect {
	private HashMap<String, TreeSelectNode> treeSelectNodesMap = new LinkedHashMap<String, TreeSelectNode>();
    private List<TreeSelectNode> treeSelectNodesList = new ArrayList<TreeSelectNode>();
    
    public TreeSelect(List<ITreeNode> list){
        initTreeNodeMap(list);
        initTreeNodeList();
    }

    private void initTreeNodeMap(List<ITreeNode> list){
    	TreeSelectNode treeSelectNode = null;
        for(ITreeNode item : list){
            treeSelectNode = new TreeSelectNode(item);
            treeSelectNodesMap.put(treeSelectNode.getKey(), treeSelectNode);
        }

        Iterator<TreeSelectNode> iter = treeSelectNodesMap.values().iterator();
        TreeSelectNode parentTreeNode = null;
        while(iter.hasNext()){
            treeSelectNode = iter.next();
            if(treeSelectNode.getParentNodeId() == null || treeSelectNode.getParentNodeId() == ""){
                continue;
            }

            parentTreeNode = treeSelectNodesMap.get(treeSelectNode.getParentNodeId());
            if(parentTreeNode != null){
                treeSelectNode.setParent(parentTreeNode);
                parentTreeNode.addChild(treeSelectNode);
            }
        }
    }

    private void initTreeNodeList(){
        if(treeSelectNodesList.size() > 0){
            return;
        }
        if(treeSelectNodesMap.size() == 0){
            return;
        }
        Iterator<TreeSelectNode> iter = treeSelectNodesMap.values().iterator();
        TreeSelectNode treeSelectNode = null;
        while(iter.hasNext()){
            treeSelectNode = iter.next();
            if(treeSelectNode.getParent() == null){
                this.treeSelectNodesList.add(treeSelectNode);
                this.treeSelectNodesList.addAll(treeSelectNode.getAllChildren());
            }
        }
    }

    @Override
    public List<TreeSelectNode> getTree() {
        return this.treeSelectNodesList;
    }

    @Override
    public List<TreeSelectNode> getRoot() {
        List<TreeSelectNode> rootList = new ArrayList<TreeSelectNode>();
        if (this.treeSelectNodesList.size() > 0) {
            for (TreeSelectNode node : treeSelectNodesList) {
                if (node.getParent() == null)
                    rootList.add(node);
            }
        }
        return rootList;
    }

    @Override
    public TreeSelectNode getTreeSelectNode(String nodeId) {
        return this.treeSelectNodesMap.get(nodeId);
    }
}
