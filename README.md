Assumptions:

1. We'll do parent check for all the values even if Sub-Section is missing,
so Section-1::Sub-section-1::Var3::Section-1::Sub-section-1::Var3 will be returning correct value via Section-1::Sub-section-1::Var3
2. The number of entries in Config file can fit in the memory. If not then we can use Guava Cache with size based LRU eviction instead of ConcurrentHashMap
3. All the base values come before the first Section
4. No names of Subsections at same level and substrings of any other name.
5. Naming convention is like SubgroupName = <Parent_group_name>_suffix
6. All Section Names  start with Section
7. All Subsection Names start with Sub Section


Usage:
1. Users will first load the config via ConfigReader.initialize("filepath"); 
2. After that the users can call the get method.

Currently I am using an in-memory data store, but there is provision to add a remote key-value store as well which can be implemented in RemoteKeyValueDataStore class
