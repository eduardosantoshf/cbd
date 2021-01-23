#from neo4j import GraphDatabase
#from py2neo import Graph
'''
class SpotifsNeo4j:
    def __init__(self, uri, user, password):
        self.driver = GraphDatabase.driver(uri, auth = (user, password))

    def close(self):
        self.driver.close()

    def insert_db(self):
        with self.driver().session as session:
            print("Inserting data set")
            session.run(
                "load csv with headers from 'file:///spotify_dataset/data_by_genres.csv' as row merge (g:Genre {genre: row.genres}) merge (k:Key {key: row.key}) merge (p:Popularity {popularity: row.popularity})"
            )
            print("Data set sucessfully inserted!")

    def insert_relations_between_nodes(self):
        print("Creating relations")
        session.run(
            "load csv with headers from 'file:///spotify_dataset/data_by_genres.csv' as line match (g:Genre {genre: row.genres}) match (k:Key {key: row.key}) match (p:Popularity {popularity: row.popularity}) merge (genre)-[:HAS_KEY]-(key) merge (genre)-[:HAS_POPULARITY]-(popularity)"
        )
        print("Relations sucessfully created!")

if __name__ == "__main__":
    connecter = SpotifsNeo4j("bolt://localhost:7687", 
                            "neo4j", 
                            "password"
                )
    connecter.insert_nodes()
    connecter.insert_relations_between_nodes()
    connecter.close()

    #with open("../CBD_L44_output.txt", "w") as printer:
'''

#graph = Graph("bolt://localhost:7474")

from neo4j import GraphDatabase

uri = "bolt://localhost:11005"
driver = GraphDatabase.driver(uri, auth=("neo4j", "spotifs"))


def test_function(tx):
    for record in tx.run("MATCH (n)"
           "RETURN n"):
        print(record)

with driver.session() as session:
    session.read_transaction(test_function)
    
driver.close()