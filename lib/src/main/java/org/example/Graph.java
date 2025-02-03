package org.example;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A basic mutable graph
 * 
 * TODO: Add all required tests and add documentation to some methods
*/

public class Graph<N, T>
{
    private Map<N, Node> nameToNode;
    private Map<N, Set<Edge>> nameToEdges;

    /**
     * Create a Graph
    */
    public Graph()
    {
        this.nameToNode = new HashMap<>();
        this.nameToEdges = new HashMap<>();
    }

    /**
     * Add edge from 'startNode' to 'endNode' with weight 'weight'
     * 
     * @param startNode
     * @param endNode
     * @param weight
     * @throws IllegalArgumentException if 'startNode' or 'endNode'
     *  are not in 'this'.
    */
    public void addEdge(N startNode, N endNode, int weight)
    {
        if (!nameToNode.containsKey(startNode) || !nameToNode.containsKey(endNode))
        {
            throw new IllegalArgumentException("Either " + startNode.toString() + " or " + endNode.toString() + " is not present.");
        }
        nameToEdges.get(startNode).add(new Edge(endNode, weight));
        nameToNode.get(endNode).incomingNodes++;
    }

    /**
     * Check if the specified edge is present
     * 
     * @param startNode start node of the specified edge
     * @param endNode end node of the specified edge
     * @throws IllegalArgumentException iff start node is not present
     * @return true iff edge is present
    */
    public boolean containsEdge(N startNode, N endNode)
    {
        if(!containsNode(startNode))
        {
            throw new IllegalArgumentException("'startNode' is not in 'this'");
        }
        return nameToEdges.get(startNode).contains(new Edge(endNode));
    }

    /**
     * Return the edges of the specified node
     * 
     * @param nodeName node whose edges are to be retrieved
     * @return a map from each name of an endNode to its corrisponding edge
    */
    public Map<N, Integer> getEdges(N nodeName)
    {
        if(!containsNode(nodeName))
        {
            throw new IllegalArgumentException("'nodeName' is not in 'this'");
        }
        Map<N, Integer> childToWeight = new HashMap<>();
        Set<Edge> edges = nameToEdges.get(nodeName);
        for (Edge edge: edges)
        {
            childToWeight.put(edge.child, edge.weight);
        }
        return childToWeight;
    }

    /**
     * Remove the specified edge
     * 
     * @param startNode start node of edge to be removed
     * @param startNode end node of edge to be removed
     * @throws IllegalArgumentException iff the edge is not present
    */
    public void removeEdge(N startNode, N endNode)
    {
        if (!nameToNode.containsKey(startNode) || !containsEdge(startNode, endNode))
        {
            throw new IllegalArgumentException("Edge cannot be removed as it is not present");
        }
        nameToEdges.get(startNode).remove(new Edge(endNode));
        nameToNode.get(endNode).incomingNodes--;
    }

    /**
     * Removes all edges from this node
     * 
     * @param startNode node whose nodes we remove
     * @throws IllegalArgeumentException iff node is not present
    */
    public void removeAllEdges(N startNode)
    {
        if (!nameToEdges.keySet().contains(startNode))
        {
            throw new IllegalArgumentException("Cannot remove edges from a node that is not present");
        }
        for (Edge edge: nameToEdges.get(startNode))
        {
            removeEdge(startNode, edge.child);
        }
    }

    /**
     * Delete the specified node
     * 
     * @param nodeName node to be deleted
     * @throws IllegalArgumentException if node is not present
     * @throws IllegalArgumentException if node has incoming edges
    */
    public void deleteNode(N nodeName)
    {
        if (!nameToEdges.keySet().contains(nodeName))
        {
            throw new IllegalArgumentException("Cannot remove node that is not present");
        }
        if (nameToNode.get(nodeName).incomingNodes != 0)
        {
            throw new IllegalArgumentException("You cannot delete a node if it have incoming edges");
        }
        removeAllEdges(nodeName);
        nameToEdges.remove(nodeName);
        nameToNode.remove(nodeName);
    }

    /**
     * true iff 'nodeName' has incoming edges
     * 
     * @param nodeName node to check if it has incoming edges
     * @throws IllegalArgumentException iff node is not present
     * @return true iff 'nodeName' has incoming edges
    */
    public boolean hasIncomingEdges(N nodeName)
    {
        if (!nameToEdges.keySet().contains(nodeName))
        {
            throw new IllegalArgumentException("Node is not present");
        }
        return nameToNode.get(nodeName).incomingNodes != 0;
    }

    /**
     * Adds the specified node with no contents
     * 
     * @param nodeName node to be added
     * @throws IllegalArgumentException iff the node is already present
    */
    public void addNode(N nodeName)
    {
        addNode(nodeName, null);
    }

    /***/
    public Set<N> getNodeNames()
    {
        return nameToNode.keySet();
    }

    /***/
    public boolean hasNoChildren(N nodeName)
    {
        return nameToEdges.get(nodeName).isEmpty();
    }

    /***/
    public T getNodeValue(N nodeName)
    {
        return nameToNode.get(nodeName).contents;
    }

    /***/
    public void setNodeValue(N nodeName, T value)
    {
        nameToNode.get(nodeName).contents = value;
    }

    /***/
    public void addNode(N nodeName, T nodeContents)
    {
        if (nameToNode.containsKey(nodeName))
        {
            throw new IllegalArgumentException("Cannot add a node that is already present");
        }
        Node node = new Node(nodeName, nodeContents);
        this.nameToNode.put(nodeName, node);
        this.nameToEdges.put(nodeName, new HashSet<Edge>());
    }

    /***/
    public boolean containsNode(N nodeName)
    {
        return nameToEdges.containsKey(nodeName);
    }

    /***/
    public int numOfChildren(N nodeName)
    {
        return nameToEdges.get(nodeName).size();
    }

    /***/
    public int size()
    {
        return nameToNode.size();
    }

    private class Node
    {
        N name;
        T contents;
        int incomingNodes;
        Node(N nodeName, T nodeContents)
        {
            this.name = nodeName;
            this.contents = nodeContents;
            this.incomingNodes = 0;
        }
    }

    private class Edge
    {
        Integer weight;
        N child;
        Edge(N child, Integer weight)
        {
            this.child = child;
            this.weight = weight;
        }
        Edge(N child)
        {
            this(child, 0);
        }
        @Override
        public int hashCode() {
            return child.hashCode();
        }
        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
            {
                return true;
            }
            if (!(obj instanceof Graph.Edge))
            {
                return false;
            }
            Graph.Edge other = (Graph.Edge)obj; 
            return this.child.equals(other.child);
        }
    }
}
