# PROTOCOL DESIGN
## GDBP - Graph DataBase Protocol
Graph DataBase Protocol (GDBP for short) is a protocol I'm designing for this project.
For simplicity, I'm treating all the DB nodes 
as points in a graph, and connections as edges. Thus,
all the algorithms are going to be implementations
of graph algorithms. 

## Communication between nodes
GDBP itself is responsible for communication between the DB nodes. It's a set of pre-defined commands,
all of which connect to an `src/utils/GDBP_OperationMas` item.

## GDBP Explained

### 0 : CON_REQ_NOD
This is a communicat sent by a DB node
when attempting to establish a connection with a
DB node to expand the network graph.

### 1 : CON_REQ_CLI
This is a communicat sent by a DB client
when attempting to establish a connection with a 
DB node. 

### 2 : CON_ACC
This communicat gets sent over from a DB node to
its client after a successful connection is 
established. _(Essentially finalizes the 
digital handshake)._

### 4 : C_SET_VAL


### 8 : C_GET_VAL


### 16 : C_FIND_KEY


### 32 : C_GET_MAX


### 64 : C_GET_MIN


### 128 : C_REWRITE


### 256 : C_KILL


### 512 : N_SET_VAL


### 1024 : N_GET_VAL


### 2048 : N_FIND_KEY


### 4096 : N_GET_MAX


### 8192 : N_GET_MIN


### 16384 : N_REWRITE


### 32768 : N_KILL

