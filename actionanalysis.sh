#!/bin/sh

GITHUB_BRANCH=${GITHUB_REF##*/}
GITHUB_PROJECT=${GITHUB_REPO##*/}
PULL_REQUEST=$(curl "https://api.github.com/repos/$GITHUB_REPO/pulls?state=open" \
  -H "Authorization: Bearer $GITHUB_TOKEN" | jq ".[] | select(.head.sha==\"$GITHUB_SHA\") | .number")
echo "Got pull request $PULL_REQUEST for branch $GITHUB_BRANCH"

# Install ShiftLeft
curl https://www.shiftleft.io/download/sl-latest-linux-x64.tar.gz > /tmp/sl.tar.gz && sudo tar -C /usr/local/bin -xzf /tmp/sl.tar.gz

curl -XPOST "https://api.github.com/repos/$GITHUB_REPO/statuses/$GITHUB_SHA" \
  -H "Authorization: Bearer $GITHUB_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"state": "pending", "context": "Code analysis"}'
  
echo $GITHUB_PROJECT
echo $GITHUB_BRANCH

sl analyze --version-id "$GITHUB_SHA" --tag branch="$GITHUB_BRANCH" --app "$GITHUB_PROJECT" --cpg --wait --force target/hello-shiftleft-0.0.1.jar

curl -XPOST "https://api.github.com/repos/$GITHUB_REPO/statuses/$GITHUB_SHA" \
  -H "Authorization: Bearer $GITHUB_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"state": "success", "context": "Code analysis"}'

curl -XPOST "https://api.github.com/repos/$GITHUB_REPO/statuses/$GITHUB_SHA" \
  -H "Authorization: Bearer $GITHUB_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"state": "pending", "context": "Vulnerability analysis"}'

VULNS=$(curl -XPOST "https://www.shiftleft.io/api/v3/public/org/$SHIFTLEFT_ORG_ID/app/$GITHUB_PROJECT/vulnerabilities/" \
  -H "Authorization: Bearer $SHIFTLEFT_API_TOKEN" | jq -c -r '[.totalResults,.lowImpactResults,.highImpactResults]')

curl -XPOST "https://api.github.com/repos/$GITHUB_REPO/statuses/$GITHUB_SHA" \
  -H "Authorization: Bearer $GITHUB_TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"state\": \"success\", \"context\": \"Vulnerability analysis\", \"target_url\": \"https://www.shiftleft.io/violationlist/$GITHUB_PROJECT?apps=$GITHUB_PROJECT&isApp=1\"}"

TOTAL=$(echo "$VULNS" | jq -c -r '.[0]')
LOW=$(echo "$VULNS" | jq -c -r '.[1]')
HIGH=$(echo "$VULNS" | jq -c -r '.[2]')

COMMENT="## Vulnerability summary\\n\\nTotal: $TOTAL\\nHigh impact: $HIGH\\nLow impact: $LOW"

curl -XPOST "https://api.github.com/repos/$GITHUB_REPO/issues/$PULL_REQUEST/comments" \
  -H "Authorization: Bearer $GITHUB_TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"body\": \"$COMMENT\"}"

BUILD_RESULT=$(curl -X GET "https://www.shiftleft.io/api/v3/public/org/$SHIFTLEFT_ORG_ID/app/$GITHUB_PROJECT/tag/branch/$GITHUB_BRANCH/build" 
  -H "Authorization: Bearer $SHIFTLEFT_API_TOKEN" 
  -H 'Accept: */*' -H 'Cache-Control: no-cache' 
  -H 'Connection: keep-alive' 
  -H 'Host: www.shiftleft.io' 
  -H 'accept-encoding: text/plain, deflate' 
  -H 'cookie: Cookie_3=value' 
  -s -b Cookie_3=value)
  
  URL="https://shiftleft.io/"
  URL+=$(echo $BUILD_RESULT | jq -r '.errorDetails')
  PR_COMMENT="Go here for more vulnerabilities details - $URL"
  
  curl -XPOST "https://api.github.com/repos/$GITHUB_REPO/issues/$PULL_REQUEST/comments" \
  -H "Authorization: Bearer $GITHUB_TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"body\": \"$PR_COMMENT\"}"

sl check-analysis --app "$GITHUB_PROJECT" --branch "$GITHUB_BRANCH"
