/*
 * Copyright 2003-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.codehaus.groovy.tools.shell.commands

import jline.Completor

import groovy.inspect.swingui.ObjectBrowser

import org.codehaus.groovy.tools.shell.InteractiveShell
import org.codehaus.groovy.tools.shell.completor.SimpleCompletor

/**
 * The 'inspect' command.
 *
 * @version $Id$
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
class InspectCommand
    extends CommandSupport
{
    InspectCommand(final InteractiveShell shell) {
        super(shell, 'inspect', '\\n')
    }
    
    protected List createCompletors() {
        return [ new InspectCommandCompletor(shell.shell.context), null ]
    }

    void execute(final List args) {
        assert args != null
        
        if (args.size() > 1) {
            io.error.println("Unexpected arguments: $args") // TODO: i18n
            return
        }
        
        def subject
        if (args.size() == 1) {
            subject = shell.shell.context.variables[args[0]]
        }
        else {
            subject = shell.shell.context.variables['_']
        }

        if (!subject) {
            io.output.println('Subject is null; nothing to inspect') // TODO: i18n
            return
        }

        if (verbose) {
            io.output.println("Launching object browser to inspect: $subject") // TODO: i18n
        }
        
        ObjectBrowser.inspect(subject)
    }
}

/**
 * Completor for the 'inspect' command.
 *
 * @version $Id$
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
class InspectCommandCompletor
    extends SimpleCompletor
{
    private final Binding binding
    
    InspectCommandCompletor(final Binding binding) {
        assert binding

        this.binding = binding
    }

    SortedSet getCandidates() {
        def set = new TreeSet()

        binding.variables.keySet().each {
            set << it
        }

        return set
    }
}
