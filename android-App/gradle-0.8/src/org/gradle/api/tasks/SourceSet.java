/*
 * Copyright 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.api.tasks;

import groovy.lang.Closure;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.FileTree;
import org.gradle.api.file.SourceDirectorySet;

import java.io.File;

/**
 * <p>A {@code SourceSet} represents a logical group of Java source and resources.</p>
 */
public interface SourceSet {
    /**
     * The name of the main source set.
     */
    String MAIN_SOURCE_SET_NAME = "main";

    /**
     * The name of the test source set.
     */
    String TEST_SOURCE_SET_NAME = "test";

    /**
     * Returns the name of this source set.
     *
     * @return The name. Never returns null.
     */
    String getName();

    /**
     * Returns the classpath used to compile this source.
     *
     * @return The classpath. Never returns null.
     */
    FileCollection getCompileClasspath();

    /**
     * Sets the classpath used to compile this source.
     *
     * @param classpath The classpath. Should not be null.
     */
    void setCompileClasspath(FileCollection classpath);

    /**
     * Returns the classpath used to execute this source.
     *
     * @return The classpath. Never returns null.
     */
    FileCollection getRuntimeClasspath();

    /**
     * Sets the classpath used to execute this source.
     *
     * @param classpath The classpath. Should not be null.
     */
    void setRuntimeClasspath(FileCollection classpath);

    /**
     * Returns the directory to assemble the compiled classes into.
     *
     * @return The classes dir. Never returns null.
     */
    File getClassesDir();

    /**
     * Sets the directory to assemble the compiled classes into.
     *
     * @param classesDir the classes dir. Should not be null.
     */
    void setClassesDir(File classesDir);

    /**
     * Returns the compiled classes directory for this source set.
     *
     * @return The classes dir, as a {@link FileCollection}.
     */
    FileCollection getClasses();

    /**
     * Registers a set of tasks which are responsible for compiling this source set into the classes directory. The
     * paths are evaluated as for {@link org.gradle.api.Task#dependsOn(Object...)}.
     *
     * @param taskPaths The tasks which compile this source set.
     * @return this
     */
    SourceSet compiledBy(Object... taskPaths);

    /**
     * Returns the non-Java resources which are to be copied into the class output directory.
     *
     * @return the resources. Never returns null.
     */
    SourceDirectorySet getResources();

    /**
     * Configures the non-Java resources for this set. The given closure is used to configure the {@code
     * SourceDirectorySet} which contains the resources.
     *
     * @param configureClosure The closure to use to configure the resources.
     * @return this
     */
    SourceSet resources(Closure configureClosure);

    /**
     * Returns the Java source which is to be compiled by the Java compiler into the class output directory.
     *
     * @return the Java source. Never returns null.
     */
    SourceDirectorySet getJava();

    /**
     * Configures the Java source for this set. The given closure is used to configure the {@code SourceDirectorySet}
     * which contains the Java source.
     *
     * @param configureClosure The closure to use to configure the Java source.
     * @return this
     */
    SourceSet java(Closure configureClosure);

    /**
     * All Java source for this source set. This includes, for example, source which is directly compiled, and source
     * which is indirectly compiled through joint compilation.
     *
     * @return the Java source. Never returns null.
     */
    FileTree getAllJava();

    /**
     * All source for this source set.
     *
     * @return the source. Never returns null.
     */
    FileTree getAllSource();

    /**
     * Returns the name of the classes task for this source set.
     *
     * @return The task name. Never returns null.
     */
    String getClassesTaskName();

    /**
     * Returns the name of the resource process task for this source set.
     *
     * @return The task name. Never returns null.
     */
    String getProcessResourcesTaskName();

    /**
     * Returns the name of a compile task for this source set.
     *
     * @param language The language to be compiled.
     * @return The task name. Never returns null.
     */
    String getCompileTaskName(String language);
}
