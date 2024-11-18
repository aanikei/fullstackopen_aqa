import pytest
from dotenv import load_dotenv
from pymongo import MongoClient
import os
import bcrypt
import jwt

TEST_DATA: dict[str, str] = { "username": "Test user", "name": "Test user", "password": "Secret123" }

@pytest.fixture
def mongo_db():
  load_dotenv()
  client = MongoClient(os.getenv('TEST_MONGODB_URI'))
  db  = client['testBlogListApp']
  users = db['users']
  blogs = db['blogs']

  users.delete_many({})
  blogs.delete_many({})

  yield db

  users.delete_many({})
  blogs.delete_many({})

@pytest.fixture
def test_user(mongo_db):
  insert_user(mongo_db, TEST_DATA)
  return TEST_DATA.copy()

def insert_user(mongo_db, user: dict[str, str]) -> None:
  hashed_password = bcrypt.hashpw(user['password'].encode('UTF-8'), bcrypt.gensalt(10))
  mongo_test_user = user.copy()
  mongo_test_user['password'] = hashed_password # type: ignore
  return mongo_db['users'].insert_one(mongo_test_user)

@pytest.fixture
def test_user_jwt(mongo_db):
  insert_result = insert_user(mongo_db, TEST_DATA)
  user_for_token = { 'username': TEST_DATA['username'], 'id': str(insert_result.inserted_id)  }
  return jwt.encode(user_for_token, os.getenv('SECRET'), algorithm="HS256")