package com.epam.sdk.figures;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by zhurlik on 11/14/14.
 */
public final class FigureFactoryTest {

    @Test
    public void testMain() throws Exception {
        FigureFactory f = new FigureFactory();

        try {
            FigureFactory.build(null);
            assert false;
        } catch (RuntimeException e) {
            assertTrue(true);
        }

        try {
            FigureFactory.build("wrong");
            assert false;
        } catch (Exception e) {
            assertEquals("Illegal figure string: wrong", e.getMessage());
            assertTrue(true);
        }

        try {
            FigureFactory.build("w1");
            assert false;
        } catch (Exception e) {
            assertEquals("Illegal figure string: w1", e.getMessage());
            assertTrue(true);
        }
    }
}
