from neo4j import GraphDatabase

class Foodys:
    def __init__(self, uri, user, password):
        self._driver = GraphDatabase.driver(uri, auth = (user, password))

    def close(self):
        self._driver.close()

    def insert_db(self):
        with self._driver.session() as session:
            print("Inserting data set")
            session.run(
                "load csv with headers from 'file:///generic-food.csv' as row with row merge (f:Food {name: row.FOOD_NAME}) merge (g:Group {group: row.GROUP}) merge (s:Sub_Group {sub_group: row.SUB_GROUP})"
            )
            print("Data set sucessfully inserted!")

    def insert_relations_between_nodes(self):
        with self._driver.session() as session:
            print("Creating relations")
            session.run(
                "load csv with headers from 'file:///generic-food.csv' as row match (f:Food {name: row.FOOD_NAME}) match (g:Group {group: row.GROUP}) merge (f)-[:HAS_GROUP]->(g)"
            )
            session.run(
                "load csv with headers from 'file:///generic-food.csv' as row match (f:Food {name: row.FOOD_NAME}) match (s:Sub_Group {sub_group: row.SUB_GROUP}) merge (f)-[:HAS_SUB_GROUP]->(s)"
            )
            print("Relations sucessfully created!")
    
    def query(self, query):
        ret = ''
        with self._driver.session() as session:
            res = session.run(query)
            for i in res:
                ret += str((i.items()[0][1]))
                ret += "\n"

        return ret
        
    
if __name__ == "__main__":
    connecter = Foodys("bolt://localhost:7687", 
                            "neo4j", 
                            "foodys"
                )
    #connecter.insert_db()
    #connecter.insert_relations_between_nodes()

    with open("./CBD_L44c_output.txt", "w") as printer:
        query1 = "match(f:Food) return f"
        printer.write("1 - " + query1 + "\n")
        printer.write("result:\n")
        q1 = connecter.query(query1)
        print(q1)
        printer.write(q1)
        printer.write("\n")

        
        query2 = "match (f:Food)-[:HAS_GROUP]->(g:Group) return f, g"
        printer.write("2 - " + query2 + "\n")
        printer.write("result:\n")
        q2 = connecter.query(query2)
        print(q2)
        printer.write(q2)
        printer.write("\n")

        query3 = "match (f:Food)-[:HAS_SUB_GROUP]->(s:Sub_Group) return f, s"
        printer.write("3 - " + query3 + "\n")
        printer.write("result:\n")
        q3 = connecter.query(query3)
        print(q3)
        printer.write(q3)
        printer.write("\n")

        query4 = "match (f:Food)-[:HAS_GROUP]->(g:Group) where f.name='Dock' return g"
        printer.write("4 - " + query4 + "\n")
        printer.write("result:\n")
        q4= connecter.query(query4)
        print(q4)
        printer.write(q4)
        printer.write("\n")

        query5 = "match (f:Food)-[:HAS_GROUP]->(g:Group) where g.group='Nuts' return f.name"
        printer.write("5 - " + query5 + "\n")
        printer.write("result:\n")
        q5 = connecter.query(query5)
        print(q5)
        printer.write(q5)
        printer.write("\n")

        query6 = "match(f:Food) return f, size(f.name)"
        printer.write("6 - " + query6 + "\n")
        printer.write("result:\n")
        q6 = connecter.query(query6)
        print(q6)
        printer.write(q6)
        printer.write("\n")

        query7 = "match(f:Food) return avg(size(f.name))"
        printer.write("7 - " + query7 + "\n")
        printer.write("result:\n")
        q7 = connecter.query(query7)
        print(q7)
        printer.write(q7)
        printer.write("\n")

        query8 = "match (f:Food)-[:HAS_GROUP]->(g:Group) where g.group <>'Herbs and Spices'and g.group <> 'Fruits' return f.name, g.group"
        printer.write("8 - " + query8 + "\n")
        printer.write("result:\n")
        q8 = connecter.query(query8)
        print(q8)
        printer.write(q8)
        printer.write("\n")

        query9 = "match (f:Food)-[:HAS_SUB_GROUP]->(s:Sub_Group) where size(s.sub_group)<4 return f, s"
        printer.write("9 - " + query9 + "\n")
        printer.write("result:\n")
        q9 = connecter.query(query9)
        print(q9)
        printer.write(q9)
        printer.write("\n")

        query10 = "match (s:Sub_Group) where s.sub_group contains 'ad' return s.sub_group"
        printer.write("10 - " + query10 + "\n")
        printer.write("result:\n")
        q10 = connecter.query(query10)
        print(q10)
        printer.write(q10)
        printer.write("\n")
        

    connecter.close()
