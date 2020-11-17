# 作业一

```JAVA
public static String findMergedNode(LinkedList<String> list1, LinkedList<String> list2) {
        String result = "";
        Stack<String> stack1 = new Stack<>();
        Stack<String> stack2 = new Stack<>();
        list1.forEach(stack1::push);
        list2.forEach(stack2::push);
        while (!stack1.isEmpty() && !stack2.isEmpty()) {
            String node1 = stack1.pop();
            String node2 = stack2.pop();
            if (!node1.equals(node2)) {
                break;
            }
            result = node1;
        }
        return result;
    }
```
