package com.lu.pojo;

import com.lu.mapper.ITreeNodeData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpeciesTreeNode<T extends ITreeNodeData> implements Serializable {
    private static final long serialVersionUID = 1L;
    private T nodeData;
    private SpeciesTreeNode<T> parent;    private List<SpeciesTreeNode<T>> children = new ArrayList<SpeciesTreeNode<T>>();
        public void addChildNode(SpeciesTreeNode<T> childNode) {
        childNode.setParent(this);
        children.add(childNode);
    }

        public void clear() {
        children.clear();
    }

        public SpeciesTreeNode<T> searchSubNodeByName(String nodeName) {
        SpeciesTreeNode<T> node = null;

        if (nodeData.getSpeciesName().equals(nodeName)) {
            node = this;
            return node;
        }

        for (SpeciesTreeNode<T> item : children) {
            node = item.searchSubNodeByName(nodeName);
            if (node != null) {
                break;
            }
        }
        return node;
    }

        public SpeciesTreeNode<T> searchSuperNodeByName(String nodeName) {
        SpeciesTreeNode<T> node = null;
        if (nodeData.getSpeciesName().equals(nodeName)) {
            node = this;
            return node;
        }

        if (parent != null) {
            node = parent.searchSuperNodeByName(nodeName);
        }

        return node;
    }

        public SpeciesTreeNode<T> searchSubNodeBySpeciesId(String speciesId) {
        SpeciesTreeNode<T> node = null;

        if (nodeData.getSpeciesName().startsWith(speciesId) && nodeData.getSpeciesName().split("  ")[0].equals(speciesId)) {
            node = this;
            return node;
        }

        for (SpeciesTreeNode<T> item : children) {
            node = item.searchSubNodeBySpeciesId(speciesId);
            if (node != null) {
                break;
            }
        }
        return node;
    }

        public SpeciesTreeNode<T> searchSuperNodeBySpeciesId(String speciesId) {
        SpeciesTreeNode<T> node = null;

        if (nodeData.getSpeciesName().equals(speciesId)) {
            node = this;
            return node;
        }

        if (parent != null) {
            node = parent.searchSuperNodeBySpeciesId(speciesId);
        }

        return node;
    }

        public SpeciesTreeNode<T> getLastCommonAncestor(SpeciesTreeNode<T> node1, SpeciesTreeNode<T> node2) {
        SpeciesTreeNode<T> temp;
        while (node1 != null) {
            node1 = node1.parent;
            temp = node2;
            while (temp != null) {
                if (node1 == temp.parent)
                    return node1;
                temp = temp.parent;
            }
        }
        return null;
    }

    
        public int getDistanceBetweenNodes(SpeciesTreeNode<T> parentNode, SpeciesTreeNode<T> childNdoe) {
        int distance = -1;
        SpeciesTreeNode<T> temp;
        int last = 1;        int count = 0;        Queue<SpeciesTreeNode<T>> queue = new LinkedList<SpeciesTreeNode<T>>();
        queue.offer(parentNode);
        while (!queue.isEmpty()) {
            temp = queue.poll();
            count++;
            if (temp == childNdoe)
                return ++distance;
            for (SpeciesTreeNode<T> node : temp.getChildren()) {
                queue.offer(node);
            }
            if (count == last) {
                distance++;
                last = count + queue.size();
            }
        }
        return -1;
    }

        public int calculateClassificationDistance(SpeciesTreeNode<T> node1, SpeciesTreeNode<T> node2) {
        SpeciesTreeNode<T> parentNode = getLastCommonAncestor(node1, node2);
        if (parentNode == null) {
            System.out.println("Two nodes do not have a parent node");
            return 99999;
        }
        int distance1 = getDistanceBetweenNodes(parentNode, node1);
        int distance2 = getDistanceBetweenNodes(parentNode, node2);
        return distance1 + distance2;
    }

    @Override
    public String toString() {
        return "SpeciesTreeNode{" +
                "nodeData=" + nodeData +
                '}';
    }

}