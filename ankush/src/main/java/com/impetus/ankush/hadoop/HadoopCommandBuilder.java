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
package com.impetus.ankush.hadoop;

/**
 * The Class HadoopCommandBuilder.
 */
public class HadoopCommandBuilder {
    
    /**
     * Creates the archive cmd.
     *
     * @param archiveName the archive name
     * @param srcDir the src dir
     * @param destDir the dest dir
     * @return the string
     */
    public String createArchiveCmd(String archiveName, String parentDir, String srcDir, String destDir) {
        StringBuilder cmd = new StringBuilder();
        if(!archiveName.endsWith(".har")){
        	archiveName += ".har";
        }
        cmd.append("archive -archiveName ").append(archiveName).append(" -p ").append(parentDir).append(" ").append(srcDir).append(" ").append(destDir);
        return cmd.toString();
    }

    /**
     * Creates the distcp cmd.
     *
     * @param sourceUrl the source url
     * @param destinationUrl the destination url
     * @param options the options
     * @return the string
     */
    public String createDistcpCmd(String sourceUrl, String destinationUrl, String options) {
        StringBuilder cmd = new StringBuilder();
        cmd.append("distcp ").append(options).append(" ").append(sourceUrl).append(" ").append(destinationUrl);
        return cmd.toString();
    }

    /**
     * Creates the balancer cmd.
     *
     * @param threshold the threshold
     * @return the string
     */
    public String createBalancerCmd(String threshold) {
        StringBuilder cmd = new StringBuilder();
        cmd.append("balancer ");
        if ((threshold != null) && (threshold.length() != 0)) {
            cmd.append("-threshold ").append(threshold);
        }

        return cmd.toString();
    }

    /**
     * Creates the fsck cmd.
     *
     * @param path the path
     * @param genericOptions the generic options
     * @param otherOptions the other options
     * @return the string
     */
    public String createFsckCmd(String path, String genericOptions, String otherOptions) {
        StringBuilder cmd = new StringBuilder();
        if(genericOptions == null) {
			genericOptions = "";
		}
        cmd.append("fsck ").append(genericOptions).append(" ").append(path).append(" ")
                .append(otherOptions);

        return cmd.toString();
    }
}
