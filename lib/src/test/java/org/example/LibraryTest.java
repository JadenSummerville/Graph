/*
 * This source file was generated by the Gradle 'init' task
 */
package org.example;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LibraryTest
{
    private Graph<String, Object> graph;
    @Test void addNodes() throws NoSuchFieldException, IllegalAccessException
    {
        checkRep();
        graph.addNode("test");
        checkRep();
        
    }

    @BeforeEach
    public void setup()
    {
        graph = new Graph<>();
    }

    private void checkRep() throws NoSuchFieldException, IllegalAccessException 
    {
        Map<String, Class<?>> nameToNode = (Map<String, Class<?>>) getPrivateField(graph, "nameToNode");
        Map<String, Set<Class<?>>> nameToEdges = (Map<String, Set<Class<?>>>) getPrivateField(graph, "nameToEdges");
        assertEquals(nameToNode.keySet(), nameToEdges.keySet());
        for (Map.Entry<String, Set<Class<?>>> entry: nameToEdges.entrySet())
        {
            assertNotNull(entry.getValue());
            for (Class<?> node: entry.getValue())
            {
                Object childNode = getPrivateField(node, "child");
                assertNotNull(childNode);
                nameToNode.containsKey(childNode);
            }
        }
    }

    private Object getPrivateField(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException
    {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }
}
