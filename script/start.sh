#! /bin/bash
base_dir=$(dirname $0)
if [ $# = 2 ] ; then

	java -jar $base_dir/../target/pojo_annotation_and_tag_generator-1.0-SNAPSHOT-shade.jar $1 $2

else 
	echo 'generate [from Directory] [to Directory]'
fi
