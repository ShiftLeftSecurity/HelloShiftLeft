#!/bin/bash
echo "Downloading ShiftLeft Agent!"
curl -O https://www.shiftleft.io/download/sl-latest-windows-x64.zip
echo "Extracting Shiftleft Agent!"
powershell -Command "& {Expand-Archive sl-latest-windows-x64.zip -DestinationPath . ;}"
echo "Setting SL authentication parameters!"
$BUILD_SOURCESDIRECTORY\\sl auth --org $ORGID --token $TOKENID
sleep 10
echo "Perform Code Analysis!"
$BUILD_SOURCESDIRECTORY\\sl analyze --app WebGoatNET-$BUILD_BUILDID --wait --cpg --csharp --dotnet-framework --csharp2cpg-args "-l info" "$BUILD_SOURCESDIRECTORY\\WebSite\WebSite.csproj"
sleep 10
