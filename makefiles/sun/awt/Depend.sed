# Unqualify std headers
# remove headers with no extension as well (e.g. C++ 94 STL headers) - robi
# s/ \/usr\/include[^ ]*\.h//g
s/ \/usr\/include[^ ]*//g

# Unqualify std Windows headers 
# remove headers with no extension as well (e.g. C++ 94 STL headers) - robi
# s/ WindowsSystemHeaderStubs[^ ]*\.h//g
s/ WindowsSystemHeaderStubs[^ ]*//g

# ####################################
# Rules to allow for generation of dependencies on an unbuilt tree...
#
# BuildStubs is populated with stubs for include files that are
# generated by the build and then dependencies are adjusted here
#
# Adjust awt_colors.h
# Change BuildStubs to .
s/ BuildStubs\/awt_colors.h/ $(OBJDIR)\/awt_colors.h/g
#
# Adjust CClassHeaders
# Change BuildStubs to CClassHeaders
s/ BuildStubs/ $(CLASSHDRDIR)/g
#
# End of unbuilt tree adjustments
# ####################################

# Remove any empty rules
/:[ ]*$/d

# Change .obj path from src to proper awt/obj or awt/obj_g directory
s/^.*\/\(.*:\)/$(OBJDIR)\/\1:/
