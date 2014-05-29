/*******************************************************************************
 * ===========================================================
 * Ankush : Big Data Cluster Management Solution
 * ===========================================================
 * 
 * (C) Copyright 2014, by Impetus Technologies
 * 
 * This is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation;
 * 
 * This software is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this software; if not, write to the Free Software Foundation, 
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 ******************************************************************************/
package com.impetus.ankush.common.domain;

import java.io.Serializable;

/**
 * Base class for Model objects. Child objects should implement toString(), equals() and hashCode().
 *
 */
public abstract class BaseObject implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7664274030610261588L;

    /**
     * Compares object equality. When using Hibernate, the primary key should not be a part of this comparison.
     *
     * @param o
     *            object to compare to
     * @return true/false based on equality tests
     */
    @Override
    public abstract boolean equals(Object o);

    /**
     * When you override equals, you should override hashCode. See "Why are equals() and hashCode() importation" for
     * more information: http://www.hibernate.org/109.html
     *
     * @return hashCode
     */
    @Override
    public abstract int hashCode();

    /**
     * Merge.
     *
     * @param baseObject the base object
     */
    public void merge(BaseObject baseObject) {
        // to be overridden by actual objects
    }
}
