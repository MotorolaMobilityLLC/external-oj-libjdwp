#
# Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
# DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
#
# This code is free software; you can redistribute it and/or modify it
# under the terms of the GNU General Public License version 2 only, as
# published by the Free Software Foundation.
#
# This code is distributed in the hope that it will be useful, but WITHOUT
# ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
# FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
# version 2 for more details (a copy is included in the LICENSE file that
# accompanied this code).
#
# You should have received a copy of the GNU General Public License version
# 2 along with this work; if not, write to the Free Software Foundation,
# Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
#
# Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
# CA 95054 USA or visit www.sun.com if you need additional information or
# have any questions.
#

# @test
# @bug 6706974
# @summary Add krb5 test infrastructure
# @run shell/timeout=300 basic.sh
#

if [ "${TESTSRC}" = "" ] ; then
  TESTSRC="."
fi
if [ "${TESTJAVA}" = "" ] ; then
  echo "TESTJAVA not set.  Test cannot execute."
  echo "FAILED!!!"
  exit 1
fi

# set platform-dependent variables
OS=`uname -s`
case "$OS" in
  Windows_* )
    FS="\\"
    ;;
  * )
    FS="/"
    ;;
esac

${TESTJAVA}${FS}bin${FS}javac -d . \
    ${TESTSRC}${FS}BasicKrb5Test.java \
    ${TESTSRC}${FS}KDC.java \
    ${TESTSRC}${FS}OneKDC.java \
    ${TESTSRC}${FS}Action.java \
    ${TESTSRC}${FS}Context.java \
    || exit 10
${TESTJAVA}${FS}bin${FS}java -Dtest.src=$TESTSRC BasicKrb5Test || exit 100
${TESTJAVA}${FS}bin${FS}java -Dtest.src=$TESTSRC BasicKrb5Test des-cbc-crc || exit 1
${TESTJAVA}${FS}bin${FS}java -Dtest.src=$TESTSRC BasicKrb5Test des-cbc-md5 || exit 3
${TESTJAVA}${FS}bin${FS}java -Dtest.src=$TESTSRC BasicKrb5Test des3-cbc-sha1 || exit 16
${TESTJAVA}${FS}bin${FS}java -Dtest.src=$TESTSRC BasicKrb5Test aes128-cts || exit 17
${TESTJAVA}${FS}bin${FS}java -Dtest.src=$TESTSRC BasicKrb5Test aes256-cts || exit 18
${TESTJAVA}${FS}bin${FS}java -Dtest.src=$TESTSRC BasicKrb5Test rc4-hmac || exit 23

exit 0
