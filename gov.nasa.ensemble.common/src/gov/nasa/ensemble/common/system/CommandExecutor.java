/*******************************************************************************
 * Copyright 2014 United States Government as represented by the
 * Administrator of the National Aeronautics and Space Administration.
 * All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package gov.nasa.ensemble.common.system;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Runtime.exec a process and wrap the output and error streams with pumping threads.
 */

public class CommandExecutor extends Thread {
       
        private boolean verbose = false;
       
        private boolean isBlocking = true;
       
        public CommandExecutor(String command, String[] envVariables) {
                this(command, envVariables, null);
        }
       
        public CommandExecutor(String command) {
                this(command, null, null);
        }
       
        public CommandExecutor(String command, File workingDirectory) {
                this(command, null, workingDirectory);
        }
       
        public CommandExecutor(String command, String[] envVariables, File workingDirectory) {
                this.command = command;
                this.envVariables = envVariables;
                this.workingDirectory = workingDirectory;
        }
       
        public CommandExecutor(String command, String[] envVariables, File workingDirectory, boolean isBlocking) {
                this(command, envVariables, workingDirectory);
                this.isBlocking = isBlocking;
        }
       
        public void setVerbose(boolean verbose) {
                this.verbose = verbose;
        }
        
        public CommandExecutor withVerbose(boolean verbose) {
            setVerbose(verbose);
            return this;
        }

        public boolean isVerbose() {
                return verbose;
        }

        @Override
        public void run() {
                InputStream ins   = null;
                InputStream errs  = null;
                OutputStream outs = null;
                try {
                        proc = null;
                        Runtime rt = Runtime.getRuntime();
                       
                        proc = rt.exec(command, envVariables, workingDirectory);
                        ins  = proc.getInputStream();
                        errs = proc.getErrorStream();
                        outs = proc.getOutputStream();
                       
                        StreamPumper stdout = new StreamPumper(ins, verbose);
                        StreamPumper stderr = new StreamPumper(errs, verbose);
                        stdout.start();
                        stderr.start();
                        if (isBlocking)
                                proc.waitFor();    //block this thread waiting for the exec process to finish
                        stdout.finished(); //signal the stream capture threads that the job is finished
                        stderr.finished();
                        stdout.join();     //block waiting for the stream capture to finish processing any last data
                        stderr.join();
                }
                catch (Exception e) {
                        exception = e;
                } finally {
                        if (ins  != null) try { ins.close();  } catch(Throwable t) { /* ignore */ }
                        if (errs != null) try { errs.close(); } catch(Throwable t) { /* ignore */ }
                        if (outs != null) try { outs.close(); } catch(Throwable t) { /* ignore */ }
                }
        }
       
        private class StreamPumper extends Thread {
               
                private boolean verbose = false;
               
                public StreamPumper(InputStream stream, boolean verbose) {
                        this.stream = stream;
                        this.verbose = verbose;
                }
               
                @Override
                public void run() {
                        try {
                                int numBytesAvailable = 0;                             
                                do {
                                        sleep(200);
                                        numBytesAvailable = stream.available();
                                        if (numBytesAvailable > 0) {
                                                buffer = new byte[numBytesAvailable];
                                                stream.read(buffer, 0, numBytesAvailable);
                                                if (verbose) {
                                                        char[] str = new char[numBytesAvailable];
                                                        for (int i = 0; i < numBytesAvailable; i++) str[i] = (char)buffer[i];
                                                        System.out.print(new String(str));
                                                }
                                        }
                                } while (!finished);
                        }
                        catch (Exception e) {
                                System.out.println("StreamPumper threw an exception:");
                                e.printStackTrace();
                        }
                }
               
                public void finished() { finished = true; }
               
                InputStream stream;
                public boolean finished = false;
                byte buffer[];

        }
       
       
        public int getReturnValue() {
                int value = 0;
               
                if (proc != null) {
                        value = proc.exitValue();
                }
               
                return value;
        }
       
        private String command;
        private String envVariables[];
        private File workingDirectory;
        private Process proc;
        public Exception exception=null;
}



