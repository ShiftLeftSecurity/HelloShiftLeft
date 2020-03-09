#!/bin/sh

GITHUB_BRANCH=${GITHUB_REF##*/}
GITHUB_PROJECT=${GITHUB_REPO##*/}
echo $GITHUB_REPO

PULL_REQUEST=$(curl "https://api.github.com/repos/$GITHUB_REPO/pulls?state=open" \
  -H "Authorization: Bearer $GITHUB_TOKEN" | jq ".[] | select(.head.sha==\"$GITHUB_SHA\") | .number")
  
echo "Got pull request $PULL_REQUEST for branch $GITHUB_BRANCH"

# Install ShiftLeft
curl https://www.shiftleft.io/download/sl-latest-linux-x64.tar.gz > /tmp/sl.tar.gz && sudo tar -C /usr/local/bin -xzf /tmp/sl.tar.gz

ls -l

echo "GITHUB SHA"=$GITHUB_SHA
echo "GITHUB BRANCH"=$GITHUB_BRANCH
echo "GITHUB PROJECT"=$GITHUB_PROJECT

# Analyze code!
sl analyze --version-id "$GITHUB_SHA" --tag branch="$GITHUB_BRANCH" --app "$GITHUB_PROJECT" --cpg --wait --force hello-shiftleft-0.0.1.jar

# Run Build rule check  
URL="https://www.shiftleft.io/violationlist/$GITHUB_PROJECT?apps=$GITHUB_PROJECT&isApp=1"
BUILDRULECHECK=$(sl check-analysis --app "$GITHUB_PROJECT" --branch "$GITHUB_BRANCH")

if [ -n "$BUILDRULECHECK" ]; then
    PR_COMMENT="Build rule failed, click here for vulnerability list - $URL"  
    curl -XPOST "https://api.github.com/repos/$GITHUB_REPO/issues/$PULL_REQUEST/comments" \
      -H "Authorization: Bearer $GITHUB_TOKEN" \
      -H "Content-Type: application/json" \
      -d "{\"body\": \"$PR_COMMENT\"}"
    exit 1
else
    PR_COMMENT="Build rule suceeded, click here for vulnerability list - $URL"  
    curl -XPOST "https://api.github.com/repos/$GITHUB_REPO/issues/$PULL_REQUEST/comments" \
      -H "Authorization: Bearer $GITHUB_TOKEN" \
      -H "Content-Type: application/json" \
      -d "{\"body\": \"$PR_COMMENT\"}"
    exit 0
fi

