package com.savor.security.jtsrm.commonBusines.controller;

import java.util.*;

/**
 * @author Liu.Tao
 * @date 2020-10-25
 */
public class ConsistentHashTest {

    private Map<Integer, VNode> vnodeMap = new HashMap<>();
    private List<Integer> vnodeList = new ArrayList<>();

    private void init(List<String> nodeList) {
        for (String nodeName : nodeList) {
            for (int i = 1; i <= 150; i++) {
                VNode node = new VNode(nodeName + i, nodeName);
                int hashCode = hash(node.getName());
                vnodeMap.put(hashCode, node);
                vnodeList.add(hashCode);
            }
        }
        vnodeList.sort(Integer::compareTo);
    }

    private int hash(String str) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < str.length(); i++) {
            hash = (hash ^ str.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        if (hash < 0) {
            hash = Math.abs(hash);
        }
        return hash;
    }

    private void add(String key, String value) {
        int hashCode = hash(key);
        if (vnodeList.contains(hashCode)) {
            VNode vnode = vnodeMap.get(hashCode);
            vnode.add(value);
        } else {
            for (int node : vnodeList) {
                if (hashCode < node) {
                    VNode vnode = vnodeMap.get(node);
                    vnode.add(value);
                    break;
                }
            }
        }
    }

    private void statistics() {
        Map<String, Integer> nodeMap = new HashMap<>();
        for (Map.Entry<Integer, VNode> entry : vnodeMap.entrySet()) {
            VNode vnode = entry.getValue();
            System.out.println("虚拟节点" + vnode.getName() + ":" + vnode.getSize());
            Integer nodeSize = nodeMap.get(vnode.getNodeName());
            if (nodeSize == null) {
                nodeSize = 0;
            }
            nodeSize += vnode.getSize();
            nodeMap.put(vnode.getNodeName(), nodeSize);
        }
        System.out.println("--------------------------------------");
        for (Map.Entry<String, Integer> entry : nodeMap.entrySet()) {
            System.out.println("物理节点" + entry.getKey() + ":" + entry.getValue());
        }
    }

    class VNode {
        String name;
        String nodeName;
        List<String> values = new ArrayList<>();

        VNode(String name, String nodeName) {
            this.name = name;
            this.nodeName = nodeName;
        }

        public String getName() {
            return this.name;
        }

        String getNodeName() {
            return this.nodeName;
        }

        public void add(String values) {
            this.values.add(values);
        }

        public Integer getSize() {
            return this.values.size();
        }

    }

    public static void main(String[] args) {
        ConsistentHashTest consistentHashTest = new ConsistentHashTest();

        List<String> nodeList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            nodeList.add("node" + i);
        }
        consistentHashTest.init(nodeList);

        for(int i = 1; i <= 1000000; i++) {
            consistentHashTest.add("key" + i, "value" + i);
        }

        consistentHashTest.statistics();

    }
}
