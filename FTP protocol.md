 FTP Protocol  
 
3.Data Transfer function \
-Different systems have different data presentation \
-User should have the option of specifying data presentation and transformation function \
-Ascii type is default type and must be accepted by all implementations \
-Image type is intended for the efficient storage and retrieval of files and for the transfer of binary data. It is recommended                     that this type be accepted by all FTP implementations.
- The logical byte size is not necessarily the same as the transfer byte size.
- A character file may be transferred to a host for one of three purposes: for printing, for storage and later retrieval, or for processing.
- There are also a second parametar; which indicate what vertical format is present :
 #NON PRINT - indicate if second paremtar is left out. No vertical format is present. This is use for files for proccesing or storage
 #CARRIAGE CONTROL (ASA) - Contains ASA (FORTRAN) vertical format control characters. 
