from pymongo import *
import pprint

# The database used is from https://gist.github.com/ajubin/d331f3251db4bd239c7a1efd0af54e38
# db: wine_db
# collection: rest

CLIENT = MongoClient()

def get_all_wines_names(collection):
    for w in collection.find({}, {"title": 1}):
        print("\n", w)

def search_wine(collection, wine_name):
    for w in collection.find({"title": wine_name}):
        print(w)


def find_wines_defined_points(collection, points):
    for w in collection.find({'points': {'$eq': points}}):
        print(w)

def main():
    db = CLIENT["wine_db"]
    collection = db["rest"]
    
    # get_all_wines_names(collection)

    # search_wine(collection, "Domaine Fernand Engel 2016 Pinot Noir Rosé (Alsace)")

    # find_wines_defined_points(collection, 95)



if __name__ == '__main__':
    main()