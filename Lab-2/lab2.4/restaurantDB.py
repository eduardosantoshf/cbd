from pymongo import *
from datetime import datetime
import pprint

CLIENT = MongoClient()

def insert_regist(collection, regist):

    inserted_id = collection.insert_one(regist).inserted_id
    pprint.pprint("Success! \n > ",inserted_id,"\n")


def modify_regist(collection, query, new_values):

    collection.update_many(query, new_values)
    pprint.pprint("Success! \n")
    

def search_regist(collection, regist):

    for r in collection.find(regist):
        pprint.pprint(r)

def create_index(collection, index, name):

    collection.create_index(index, name = name)

    for i in collection.index_information(): 
        print(i,"\n")

def count_localidades(collection):
    result = collection.aggregate([{"$group": {"_id": "$localidade"}}])
    
    l = list(result)

    return len(l)

def count_rest_by_localidade(collection):
    result = collection.aggregate([{"$group": {"_id": "$localidade", "numero_restaurants": {"$sum": 1}}}])
    
    return ["-> " + str(document["_id"]) + " - " + str(document["numero_restaurants"]) for document in list(result)]

def count_rest_by_localidade_by_gastronomia(collection):
    result = collection.aggregate([{"$group": {"_id": {"localidade": "$localidade","gastronomia": "$gastronomia"}, "numero_restaurants": {"$sum": 1}}}])

    return ["-> " + str(r["_id"]["localidade"]) + " | " + str(r["_id"]["gastronomia"]) + " - " + str(r["numero_restaurants"]) for r in list(result)]

def get_rest_with_name_closer_to(collection, name):
    result = collection.aggregate([{"$match": {"nome": {"$regex": name}}}])
    
    return ["-> " + str(document["nome"]) for document in list(result)]

def main():
    db = CLIENT["cbd"]

    # insert_regist(db["rest"], {"address": {"building": "24335", "coord": [70.3, 56.5], "rua": "Ruazita", "zipcode": "98567"}, "localidade": "Aweilo", "gastronomia": "Aweilense", "grades": [{"date": datetime(2020, 6, 12, 0, 0), "grade": "A", "score": 100}, {"date": datetime(
    #    2020, 6, 7, 0, 0), "grade": "A", "score": 100}], "nome": "Lollipop", "restaurant_id": "48257234"})

    # modify_regist(db["rest"], {"localidade": "Aweilo"},{"$set": {"rua": "Ria"}})

    # search_regist(db["rest"], {"localidade": "Aweilo"})

    # create_index(db["rest"], "localidade", "localidade")
    # create_index(db["rest"], "gastronomia", "gastronomia")
    # create_index(db["rest"],[("nome",TEXT)],"nome")

    print(count_localidades(db["rest"]))

    print("\nNumero de restaurantes por localidade:")

    for c in count_rest_by_localidade(db["rest"]): print(c)

    print("\nNumero de restaurantes por localidade e gastronomia:")

    for c in count_rest_by_localidade_by_gastronomia(db["rest"]): print(c)

    print("\nNome de restaurantes contendo 'Park' no nome:")
    for r in get_rest_with_name_closer_to(db["rest"], "Park"): print(r)

if __name__ == '__main__':
    main()