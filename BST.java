import java.io.*;
import java.util.*;

interface BinaryOperator<T> {
    T apply(T t1, T t2);
}

class BSTFind<T> {
    // null если в дереве вообще нету узлов
    public BSTNode<T> Node;

    // true если узел найден
    public boolean NodeHasKey;

    // true, если родительскому узлу надо добавить новый левым
    public boolean ToLeft;

    public BSTFind() {
        Node = null;
        NodeHasKey = false;
        ToLeft = false;
    }

    public BSTFind(BSTNode<T> node, boolean nodeHasKey, boolean toLeft) {
        Node = node;
        NodeHasKey = nodeHasKey;
        ToLeft = toLeft;
    }

    public BSTFind(BSTNode<T> node, boolean nodeHasKey) {
        Node = node;
        NodeHasKey = nodeHasKey;
        ToLeft = false;
    }

    public BSTNode<T> getNode() {
        return Node;
    }

    public boolean isNodeHasKey() {
        return NodeHasKey;
    }

    public boolean isToLeft() {
        return ToLeft;
    }
}


class BSTNode<T> {
    public int NodeKey; // ключ узла
    public T NodeValue; // значение в узле
    public BSTNode<T> Parent; // родитель или null для корня
    public BSTNode<T> LeftChild; // левый потомок
    public BSTNode<T> RightChild; // правый потомок

    public BSTNode(int key, T val, BSTNode<T> parent)
    {
        NodeKey = key;
        NodeValue = val;
        Parent = parent;
        LeftChild = null;
        RightChild = null;
    }

    public BSTNode(int nodeKey, T nodeValue) {
        NodeKey = nodeKey;
        NodeValue = nodeValue;
        Parent = null;
        LeftChild = null;
        RightChild = null;
    }

    public BSTNode<T> getParent() {
        return Parent;
    }

    public BSTNode<T> getLeftChild() {
        return LeftChild;
    }

    public BSTNode<T> getRightChild() {
        return RightChild;
    }

    public int getKey() {
        return NodeKey;
    }

    public T getValue() {
        return NodeValue;
    }

    public void setLeftChild(BSTNode<T> leftChild) {
        LeftChild = leftChild;
    }

    public void setRightChild(BSTNode<T> rightChild) {
        RightChild = rightChild;
    }

    public void setParent(BSTNode<T> parent) {
        Parent = parent;
    }
}

class BST<T> {
    BSTNode<T> Root; // корень дерева, или null

    public BST(BSTNode<T> node) {
        Root = node;
    }

    public BSTFind<T> FindNodeByKey(int key) {
        // ищем в дереве узел и сопутствующую информацию по ключу
        if (getRoot() == null) {
            return new BSTFind<>(null, false);
        }
        return recursiveTreeCrawlerNodeByKey(getRoot(), key);
    }

    private BSTFind<T> recursiveTreeCrawlerNodeByKey(BSTNode<T> rootNode, int key) {
        BSTFind<T> node = null;
        //The node found
        if (rootNode.getKey() == key) {
            return new BSTFind<>(rootNode, true);
        } else {
            //The node not found
            //Checking which branch to go on

            boolean addToLeft = false;
            BSTNode<T> child;
            //Choose which branch to go
            if (key > rootNode.getKey()) {
                child = rootNode.getRightChild();
            } else {
                child = rootNode.getLeftChild();
                addToLeft = true;
            }
            if (child != null) {
                //Follow the right branch
                node = recursiveTreeCrawlerNodeByKey(child, key);
            } else {
                //The node is not found. Needs to be added
                return new BSTFind<>(rootNode, false, addToLeft);
            }
        }
        return node;
    }

    public boolean addArray(BSTNode<T>... arrNodes) {
        for (BSTNode<T> nodeAdd : arrNodes) {
            AddKeyValue(nodeAdd.getKey(), nodeAdd.getValue());
        }
        return true;
    }

    public boolean AddKeyValue(int key, T val) {
        BSTFind<T> findNode = FindNodeByKey(key);
        BSTNode<T> nodeAdd = findNode.getNode();

        //The tree has no nodes. Add root
        if (findNode.getNode() == null) {
            addRoot(key, val);
            return true;
        }
        //Add node
        if (!findNode.isNodeHasKey()) {
            addNode(nodeAdd, findNode.isToLeft(), key, val);
            return true;
        }
        //The node already exists
        if (findNode.isNodeHasKey()
                && nodeAdd.getKey() == key) {
            return false;
        }
        return false; // если ключ уже есть
    }

    public void setRoot(BSTNode<T> root) {
        Root = root;
    }

    private void addRoot(int key, T val) {
        setRoot(new BSTNode<>(key, val, null));
    }

    private void addNode(BSTNode<T> nodeParent, boolean toLeft, int key, T val) {
        BSTNode<T> nodeNew = new BSTNode<>(key, val, nodeParent);
        if (toLeft) {
            nodeParent.setLeftChild(nodeNew);
        } else {
            nodeParent.setRightChild(nodeNew);
        }
    }

    public BSTNode<T> FinMinMax(BSTNode<T> FromNode, boolean FindMax) {
        // ищем максимальный/минимальный ключ в поддереве
        BinaryOperator<BSTNode<T>> minMaxOperator = getMinMaxBinaryOperator(FindMax);
        return recursiveTreeCrawlerMinMax(FromNode, FindMax, minMaxOperator);
    }

    //A function that returns at most or at least of two. Depends on the flag FindMax
    private BinaryOperator<BSTNode<T>> getMinMaxBinaryOperator(boolean FindMax) {
        BinaryOperator<BSTNode<T>> minMaxOperator;

        if (FindMax) {
            minMaxOperator = (x1, x2) -> {
                if (x1.getKey() > x2.getKey()) {
                    return x1;
                } else {
                    return x2;
                }
            };
        } else {
            minMaxOperator = (x1, x2) -> {
                if (x1.getKey() < x2.getKey()) {
                    return x1;
                } else {
                    return x2;
                }
            };
        }
        return minMaxOperator;
    }

    private BSTNode<T> recursiveTreeCrawlerMinMax(BSTNode<T> fromNode, boolean findMax, BinaryOperator<BSTNode<T>> minMaxOperator) {
        BSTNode<T> nodeMinMax = fromNode;
        //Shows where to go
        BSTNode<T> nodeCurrent;

        if (findMax) {
            nodeCurrent = fromNode.getRightChild();
        } else {
            nodeCurrent = fromNode.getLeftChild();
        }

        if (nodeCurrent != null) {
            nodeMinMax = minMaxOperator.apply(nodeMinMax, nodeCurrent);
            nodeMinMax = recursiveTreeCrawlerMinMax(nodeCurrent, findMax, minMaxOperator);
        } else {
            return nodeMinMax;
        }
        return nodeMinMax;
    }

    private void deleteNoChildren(BSTNode<T> nodeDelete) {
        if (nodeDelete == getRoot()) {
            setRoot(null);
        } else {
            if (nodeDelete == nodeDelete.getParent().getRightChild()) {
                nodeDelete.getParent().setRightChild(null);
            } else {
                nodeDelete.getParent().setLeftChild(null);
            }
        }
    }

    private void delete1Child(BSTNode<T> nodeDelete, boolean toLeft) {
        if (nodeDelete == getRoot()) {
            if (toLeft) {
                setRoot(nodeDelete.getLeftChild());
            } else {
                setRoot(nodeDelete.getRightChild());
            }
            getRoot().setParent(null);
        } else {
            //Checking the right or left child is the deleted node
            //The node to be removed is the right child
            if (nodeDelete == nodeDelete.getParent().getRightChild()) {
                if (toLeft) {
                    nodeDelete.getParent().setRightChild(nodeDelete.getLeftChild());
                    nodeDelete.getLeftChild().setParent(nodeDelete.getParent());
                } else {
                    nodeDelete.getParent().setRightChild(nodeDelete.getRightChild());
                    nodeDelete.getRightChild().setParent(nodeDelete.getParent());
                }
                //The node to be removed is the left child
            } else {
                if (toLeft) {
                    nodeDelete.getParent().setLeftChild(nodeDelete.getLeftChild());
                    nodeDelete.getLeftChild().setParent(nodeDelete.getParent());
                } else {
                    nodeDelete.getParent().setLeftChild(nodeDelete.getRightChild());
                    nodeDelete.getRightChild().setParent(nodeDelete.getParent());
                }
            }
        }
    }

    private void delete2Children(BSTNode<T> nodeDelete) {
        //Find minimum key in right branch
        BSTNode<T> nodeMinKeyRightBranch = FinMinMax(nodeDelete.getRightChild(), false);
        //We put the leaf instead of the node to be remove
        if (nodeDelete.getParent() == null) {
            //NodeDelete is the root
            setRoot(nodeMinKeyRightBranch);
        } else {
            if (nodeMinKeyRightBranch.getParent() != nodeDelete) {
                nodeMinKeyRightBranch.getParent().setLeftChild(null);
            } else {
                nodeMinKeyRightBranch.getParent().setRightChild(null);
            }
            //NodeDelete isn't the root and has a parent
            //Determine the right or left child is the node to be deleted
            if (nodeDelete.getParent().getRightChild() == nodeDelete) {
                //The right child
                nodeDelete.getParent().setRightChild(nodeMinKeyRightBranch);
            } else {
                //The left child
                nodeDelete.getParent().setLeftChild(nodeMinKeyRightBranch);
            }
            //Set a parent
            nodeMinKeyRightBranch.setParent(nodeDelete.getParent());
            nodeDelete.setParent(null);

            nodeMinKeyRightBranch.setLeftChild(nodeDelete.getLeftChild());
            nodeMinKeyRightBranch.setRightChild(nodeDelete.getRightChild());
            nodeDelete.getLeftChild().setParent(nodeMinKeyRightBranch);
            nodeDelete.getRightChild().setParent(nodeMinKeyRightBranch);
        }
    }

    public boolean DeleteNodeByKey(int key) {
        // удаляем узел по ключу
        BSTFind<T> nodeFind = FindNodeByKey(key);
        BSTNode<T> nodeDelete = nodeFind.getNode();

        if (nodeDelete == null
                || !nodeFind.isNodeHasKey()) {
            return false;
        }

        if (nodeDelete.getLeftChild() == null && nodeDelete.getRightChild() == null) {
            deleteNoChildren(nodeDelete);
        }

        if (nodeDelete.getRightChild() == null ^ nodeDelete.getLeftChild() == null) {
            delete1Child(nodeDelete, nodeDelete.getLeftChild() != null);
        }

        if (nodeDelete.getRightChild() != null && nodeDelete.getLeftChild() != null) {
            delete2Children(nodeDelete);
        }
        return true;
    }

    public int Count() {
        if (getRoot() != null) {
            return 1 + recursiveTreeCrawlerCounter(getRoot());
        }
        return 0;
    }

    private int recursiveTreeCrawlerCounter(BSTNode<T> rootNode) {
        int counter = 0;
        BSTNode<T> nodeLeft = rootNode.getLeftChild();
        BSTNode<T> nodeRight = rootNode.getRightChild();

        if (nodeLeft == null && nodeRight == null) {
            return counter;
        } else {
            if (nodeLeft != null) {
                counter += 1 + recursiveTreeCrawlerCounter(nodeLeft);
            }
            if (nodeRight != null) {
                counter += 1 + recursiveTreeCrawlerCounter(nodeRight);
            }
        }
        return counter;
    }

    public BSTNode<T> getRoot() {
        return Root;
    }

}
