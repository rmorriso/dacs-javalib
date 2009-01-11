This project is a native NetBeans project.

The file build.xml overrides two targets:

   <target name="-pre-compile">
        <delete dir="src/com/fedroot/dacs/xmlbeans/" />
        <taskdef name="xmlbean" classname="org.apache.xmlbeans.impl.tool.XMLBean"
                 classpath="${javac.classpath}"/>
        <xmlbean schema="dtd-xsd/dacs" destfile="lib/dacs-xmlbeans.jar" classpath="${javac.classpath}"
                 source="1.5" srcgendir="${src.dir}" failonerror="true"/>
    </target>
    <target name="-pre-init">
        <property name="default.javac.source" value="1.5"/>
        <property name="default.javac.target" value="1.5"/>
    </target>

The first target forces javac to source=1.5 and the second is a target
to build xmlbeans from the XML schema in dtd-xsd/dacs.

Build the project in NetBeans or - in this directory - from the command line:

Clean the project:

  % ant -f freeform-build.xml clean

Build the jar:

  % ant -f freeform-build.xml jar
