#!/bin/sh
java -classpath "lib/VDM.jar:lib/org.overturetool.parser.jar:overtureUtility.jar:lib/org.overturetool.umltrans-1.0.0.jar" MainClass -vdmjEx -vdmjDir "../VDMJmodel" -project "../main/vpp/CT.prj" -vdmjSpec "../VDMJmodel/vdmjSpecificModel" -argumentFile "../VDMJmodel/vdmArgumentLinux.sh"