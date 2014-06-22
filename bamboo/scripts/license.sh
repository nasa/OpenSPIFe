#!/bin/bash
shopt -s extglob nullglob

# Usage: find.sh
#
# Finds one Ensemble Eclipse RCP product archive in the current working
# directory and searches it for files commonly known to contain license
# information. 

patterns=("EPL-V10" "Apache Software License 2.0" "Copyright")
filenames=("about.html" "asl-v20.txt" "epl-v10.html" "license.html" "notice.html")

ARCHIVES=(*.*-*-*-+([[:digit:]]).r+([[:digit:]])-*.*.*.zip)

# find one product archive
#
if [[ ${#ARCHIVES[@]} == 0 ]]
then
    echo "$0 error: no archives found."
    exit 1
fi
echo "Found ${#ARCHIVES[@]} archive(s)."
ARCHIVE="${ARCHIVES[0]}"
echo "Using $ARCHIVE"

# don't dirty up the build directory
#
TMPDIR=`mktemp -d /tmp/foo.XXXX`
trap "rm -rf $TMPDIR" exit
echo "Unarchiving files ..."
unzip -q $ARCHIVE -d $TMPDIR
for jar in `find $TMPDIR -name "*.jar"`
do
    pushd ${jar%/*} > /dev/null # file path
    mkdir ${jar%.*} > /dev/null # file head
    cd ${jar%.*}
    jar xf $jar
    rm $jar
    popd > /dev/null
done

# find common license files
#
pushd $TMPDIR > /dev/null

echo "Finding licenses ..."
for pattern in "${patterns[@]}"
do
    echo "Found $pattern in ..."
    for filename in "${filenames[@]}"
    do
	find . -iname $filename -exec grep -il "$pattern" {} \;
    done
done

popd > /dev/null

# End
