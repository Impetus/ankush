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
package com.impetus.ankush.hadoop.scheduler;

import java.io.StringWriter;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.transform.OutputKeys;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

import com.impetus.ankush.hadoop.config.Parameter;
import com.jamesmurty.utils.XMLBuilder;

/**
 * The Class SchedulerConfig.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "schedulerClassName")
@JsonSubTypes({ 
    @JsonSubTypes.Type(value = FairSchedulerConfig.class, name = "org.apache.hadoop.mapred.FairScheduler"),
    @JsonSubTypes.Type(value = CapacitySchedulerConfig.class, name = "org.apache.hadoop.mapred.CapacityTaskScheduler"),
    @JsonSubTypes.Type(value = DefaultSchedulerConfig.class, name = "default")
})
public abstract class SchedulerConfig {
    
    /** The scheduler class name. */
    private String schedulerClassName = "default";

    /**
     * Instantiates a new scheduler config.
     *
     * @param schedulerClassName the scheduler class name
     */
    protected SchedulerConfig(String schedulerClassName) {
        this.schedulerClassName = schedulerClassName;
    }

    /**
     * Instantiates a new scheduler config.
     */
    public SchedulerConfig() {
    }

    /**
     * Gets the scheduler class name.
     *
     * @return the scheduler class name
     */
    @XmlTransient
    public String getSchedulerClassName() {
        return schedulerClassName;
    }

    /**
     * Sets the scheduler class name.
     *
     * @param schedulerClassName the new scheduler class name
     */
    public void setSchedulerClassName(String schedulerClassName) {
        this.schedulerClassName = schedulerClassName;
    }
    
    /**
     * Parameters to string.
     *
     * @param parameters the parameters
     * @return the string
     * @throws Exception the exception
     */
    public static String parametersToString(List<Parameter> parameters) throws Exception {
        XMLBuilder builder = XMLBuilder.create("configuration");
        for (Parameter parameter : parameters) {
            builder.e("property").e("name").text(parameter.getName()).up().e("value").text(parameter.getValue()).up();
        }

        StringWriter writer = new StringWriter();
        Properties properties = new Properties();
        properties.put(OutputKeys.INDENT, "yes");
        builder.toWriter(writer, properties);
        return writer.toString().replaceAll("\\\"", "\\\\\"");
    }
    
    /**
     * To xml.
     *
     * @return the string
     * @throws Exception the exception
     */
    public abstract String toXML() throws Exception;
    
    /**
     * To parameters.
     *
     * @return the list
     */
    public abstract List<Parameter> toParameters();
    
    /**
     * All parameters.
     *
     * @return the list
     */
    public abstract List<Parameter> allParameters();
}
