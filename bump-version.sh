version=$1
if [ -z $version ]
then
    echo "Please provide a version"
    exit 1
fi
sed -i -e "0,/<version>/{s/<version>.*<\\/version>/<version>$version<\\/version>/}" ./pom.xml
sed -i -e "s/moduleVersion=.*/moduleVersion=$version/" ./src/main/resources/META-INF/nf-test-plugin