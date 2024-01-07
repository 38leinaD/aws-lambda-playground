#!/bin/bash
set -o pipefail # pipe fails with first failing command
set -o nounset # fail when unset vars are read
set -o errexit # exit script on failed command

export CDK_DIR=deployment/aws

all() {
    precheck

    _log_info "Build application..."
    ./gradlew :app:build --info

    _log_info "Provision AWS infra via CDK..."
    pushd deployment/aws
    cdk deploy --all --outputs-file cdk-outputs.json --require-approval=never
    popd
}

_source_cdk_output() {
    jq -r ".\"aws-playground\" | to_entries[] | select(.key | startswith(\"$1\")) | .value" $CDK_DIR/cdk-outputs.json
}

destroy() {
    pushd deployment/aws
    cdk destroy --require-approval=never
    popd
}

precheck() {
    if ! which cdk; then
        _log_warning "No CDK CLI installed. Run 'npm install -g aws-cdk' to install."
        _log_warning "Run 'cdk bootstrap' under deployment/aws folder after the install.'"
        exit 1
    fi
}

_log_start() {
    printf "%s" "$1"
}

_log_fail() {
    printf " \E[31m%s\E[0m\n" "Failed"
}

_log_ok() {
    printf " \E[32m%s\E[0m\n" "Ok"
}

_log_info() {
    printf "\E[1;37m%s\E[0;0m\n" "$1"
}

_log_warning() {
    printf "\E[1;33m%s\E[0;0m\n" "$1"
}

set +o nounset
if [ -n "$COMP_LINE" ]
then
    compgen -A function | grep -v "^_" | tr '\n' ' '
elif [[ $1 != "" ]];
then
    set -o nounset
    FUNC=$1
    shift
    $FUNC $@
else
    set -o nounset
    echo -e "\E[1;37musage: deploy.sh <command> [args]\E[0;0m"
    echo "commands are:"

    for c in $(compgen -A function);
    do
        if [[ $c != _* ]];
        then
            echo " * $c"
        fi
    done

    _log_warning "Tip: Run 'complete -C $(pwd)/deploy.sh deploy.sh' to get bash completion!"
fi