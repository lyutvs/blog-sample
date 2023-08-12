# MongoDB 개요

## SQL vs NoSQL

### RDBMS 장단점

* 장점
  * 데이터 중복을 방지할 수 있다.
  * join 성능이 좋다
  * 복잡하고 다양한 쿼리가 가능하다
  * 잘못 입력을 방지할 수있다.
* 단점
  * 하나의 레코드를 확인하기 위해 여러 테이블을 join 하여 가시성이 떨어진다.
  * 스키마가 엄격하여 변경에대한 공수가 크다.
  * Scale-Out이 가능하지만, 설정이 어려워 전통적으로 Sale-Up 위주로 확장 했다.
  * 확장할 때마다 App단의 수정이 필요하다

### NoSQL 장단점

* 장점
  * 데이터 접근성과 가시성이 좋다.
  * Join없이 조회가 가능해서 응답 속도가 일반적으로 빠르다.
  * 스키마 변경에 공수가 적다
  * 스키마가 유연해서 데이터 모델을 App 요구사항에 맞게 데이터를 수용할 수 있다.
  * HA와 Sharding에 대한 솔루션을 자체적으로 지원하고 있어 Scale-Out이 간편하다.
  * 확장시 App의 변셩사항이 없다.
* 단점
  * 데이터중복이 발생한다.
  * 스키마가 자유롭지만, 스키마 설계를 잘해야 성능 저하를 피할 수 있다.

### Summary

* MongoDB는 Document 지향 Database이다.
* 데이터 중복이 발생할 수 있지만, 접근성과 가시성이 좋다.
* 스키마 설계가 어렵지만, 스키마가 유연해서 App의 요구사항에 맞게 데이터를 수용할 수 있다.
* 분산에 대한 솔루션을 자체적으로 지원해서 Scale-Out이 쉽다.
* 확장 시, App를 변경하지 않아도 된다.

## MongoDB 구조

### RDBMS vs MongoDB

| RDBMS    | MongoDB    |
|----------|------------|
| Cluster  | Cluster    |
| Database | Database   |
| Table    | Collection |
| Row      | Document   |
| Column   | Field      |

### 기본 Database

| Database | Description                                                                                                                                                                     |
|----------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| admin    | * 인증과 권한 부여 역할이다.<br/> * 일부 관리 작업을 하려면 admin database 접근이 필요하다.                                                                                                                 |
| local    | * 모든 mongodb instance는 local database를 소유한다.<br/> * oplog와 같은 replication 절차에 필요한 정보를 저장한다. <br/> * startup_log와 같은 instance 진단 정보를 저장한다. <br/> * local database는 자체는 복제되지 않는다. |
| config   | * shareded cluster에 각 shard 정보를 저장한다.                                                                                                                                           |

### Collection 특징

* 동적 스키마를 갖고 있어 스키마를 수정하려면 필드 값을 추가/수정/삭제 하면 된다.
* Collection 단위로 Index를 생성할 수 있다.
* Collection 단위로 Shard를 나눌 수 있다.

### Document 특징

* JSON 형태로 표한하고 BSON(Binary JSON) 형태로 저장한다.
* 모든 Document는 "_id" 필드가 있고, 없이 생성하면 ObjectId 타입의 고유한 값을 저장한다.
* 생성시, 상위 구조인 Database나 Collection이 없다면 먼저 생성하고 Document를 생성한다.
* Document의 최대 크기는 **16MB** 이다.

## MongoDB 배포 형태 소개

## Replica Set

## Sharded Cluster

* 장점
  * 용량의 한계를 극복 가능
  * 데이터 규모와 부하가 크더라도 처리량이 좋다.
  * 고가용성을 보장한다.
  * 하드웨어에 대한 제약을 해결할 수 있다.
* 단점
  * 관리가 비교적 복잡하다.
  * Replica Set과 비교해서 쿼리가 느리다.

### Architecture

![](images/draw-mongodb.drawio-1.png)

## Replica Set vs Sharded Cluster 어떻게 배포할것인가?

## MogoDB Storage Engines

| 항목               | MMAPv1                                | WiredTiger        |
|------------------|---------------------------------------|-------------------|
| Data Compression | 지원하지 않는다.                             | 지원한다.             |
| Locl             | 버전에 따라 Database 혹인 Collection 레벨 Lock | Document 레벨의 Locl |

# Document Query 실습

## CRUD

<details>
<summary>접기/펼치기</summary>
<div markdown="1">

```js
show
dbs

use
test

db.employees.insertOne({
    name: "lake",
    age: 21,
    dept: "Database",
    joinDate: new ISODate('2022-10-01'),
    salary: 400000,
    bonus: null
})

db.employees.find()


db.employees.insertMany([
    {
        name: "ocean",
        age: 45,
        dept: "Network",
        joinDate: new ISODate('1999-11-15'),
        resignationDate: new ISODate('2002-12-23'),
        salary: 100000,
        bonus: null
    },
    {
        name: "river",
        age: 34,
        dept: "Devops",
        isNegotiating: true
    }
])

db.employees.updateOne(
    {"name": "river"},
    {
        $set: {
            salary: 35000,
            dept: "Database",
            joinDate: new ISODate("2022-12-31")
        },
        $unset: {
            isNegotiating: ""
        }
    }
)

db.employees.find()

db.employees.updateMany(
    {
        resignationDate: {$exists: false},
        joinDate: {$exists: true}
    },
    {
        $mul: {salary: Decimal128("1.1")}
    }
)

db.employees.updateMany(
    {
        resignationDate: {$exists: false},
        bonus: {$exists: true}
    },
    {
        $set: {bonus: 200000}
    }
)

db.employees.find()

db.employees.deleteOne(
    {
        name: "river"
    }
)

db.employees.find()

db.employees.deleteMany({})

show
collections

db.employees.drop()


db.planets.findOne(
    {
        name: "Mars"
    }
)


db.planets.find(
    {
        hasRings: true,
        orderFromSun: {$lte: 6}
    }
)


db.planets.find(
    {
        $and: [
            {
                hasRings: true,
            },
            {
                orderFromSun: {$lte: 6}
            }
        ]
    }
)

db.planets.find(
    {
        $or: [
            {
                hasRings: {$ne: false},
            },
            {
                orderFromSun: {$gt: 6}
            }
        ]
    }
)

db.planets.find(
    {
        mainAtmosphere: {$in: ['O2']}
    }
)


```

</div>
</details>

### 유용한 Query 함수들

#### Bulk Writer

<details>
<summary>접기/펼치기</summary>
<div markdown="1">

```js
db.bulk.bulkWrite(
    [
        {insertOne: {document: {doc: 1, order: 1}}},
        {insertOne: {document: {doc: 2, order: 2}}},
        {insertOne: {document: {doc: 3, order: 3}}},
        {insertOne: {document: {doc: 4, order: 4}}},
        {insertOne: {document: {doc: 4, order: 5}}},
        {insertOne: {document: {doc: 5, order: 6}}},
        {
            deleteOne: {
                filter: {doc: 3}
            }
        },
        {
            updateOne: {
                filter: {doc: 2},
                update: {
                    $set: {doc: 12}
                }
            }
        }
    ],
    {ordered: false}
)
```

* ordered 순서 여부

```js
db.bulk.countDocuments()
db.bulk.estimatedDocumentCount()

db.bulk.distinct("doc")

db.bulk.find()

db.bulk.findAndModify(
    {
        query: {doc: 5},
        sort: {order: -1},
        update: {$inc: {doc: 1}}
    }
)

db.sequence.insertOne({seq: 0})

db.sequence.find()

db.sequence.findAndModify({
    query: {},
    sort: {seq: -1},
    update: {$inc: {seq: 1}}
})

db.bulk.getIndexes()

db.bulk.createIndex({doc: 1})

db.bulk.updateOne(
    {doc: 1},
    {$set: {_id: 1}}
)


db.bulk.replaceOne(
    {doc: 1},
    {doc: 13},
)

db.bulk.find()
```

</div>
</details>

### 배열과 내장 Document를 다루는 방법 1 Read

### 배열과 내장 Document를 다루는 방법 2 Update

### Query 예제

<details>
<summary>접기/펼치기</summary>
<div markdown="1">

#### sample_mflix database의 mpvies collection 전체를 조회한다.

```
db.movies.find()
```

#### mpvies collection의 Document 수를 구한다.

```
db.movies.countDocuments()
```

#### mpvies collection의 전체를 조회하는데, title, year, genres, runtime, rated를 보여주고 _id는 보여주지 않는다.

```
db.movies.find(
    {},
    {
        title:1,
        year:1,
        genres: 1,
        runtime: 1,
        rated: 1,
        _id:0
    }
    )
```

#### movies 100분 이하

```
db.movies.find(
    { runtime: {$lte: 100}}
)
```

#### movies 100 분이하, 장르 드라마

```
db.movies.find(
    {
        runtime: {$lte: 100},
        genres: 'Drama'
    }
)
```

#### movies 100 분이하, 장르 드라마, size 1개

```
db.movies.find(
    {
        $and: [
            {runtime: {$lte: 100}},
            {genres: 'Drama'},
            {genres: {$size: 1}}
        ]
    },
    {genres: 1}
)
```

</div>
</details>

## 집계 프레임워크 Aggregation

* Collection의 데이터를 변환하거나 분석하기 위해 집계 프레임워크
* Aggregation find 함수로 처리할 수 없는, SQL의 Group By와 Join 구문 같은 복잡한 데이터 분석 기능을 제공한다.
* Aggregation 프레임워크는 Pipelin 형태를 갖춘다.
* MongoDB 2.2 부터 제공되었고 이전에는 Map Reduce를 사용 했다.

### 자주 사용되는 Aggregation Stage

### Aggregation 예제

### 배포의 형태에 따른 CRUD 특징

# MongoDB의 일관성 제어 소개

Single Document -> Transaction -> Replica Set Member -> Sharded Cluster Shard

## Read Preference

![img.png](images/img.png)

| Read Preference 종류 | 설명                                  |
|--------------------|-------------------------------------|
| primary            | 무조건 primary로 읽기 요청                  |
| primaryPreferred   | 가능하면 primary에서 읽고 없으면 Secondary로 요청 |
| secondary          | 무조건 secondary로 읽기 요청                |
| secondaryPreferred | 가능하면 Secondary에서 읽고 없으면 primary로 요청 |
| nearest            | 평균 Ping 시간을 기반으로 지연율이 가장 낮은 멤버로 요청  |

 
## Read/Write Concern


### Write Concern
![img.png](images/img-1.png)

* w: majority 과반수의 secondary에서 apply를 한 경우 동기화 완료처리, 위 상황에서는 primary 1개 secondary가 2개이기 때문에 1개만 apply 완료되면 replica set에 적용이 완료됐다고 판단 
 
### Read Concern Level

| Read Concern 종류 | 설명                                     |
|-----------------|----------------------------------------|
| local           | 복제를 확인하지 않고 요청한 Member의 현재 데이터를 반환     |
| available       | local과 동일하지만, 고아 Document를 반환할 수 있다.   |
| majority        | Member 과반수가 들고 있는 동일한 데이터를 반환          |
| lineeaizable    | 쿼리 수행 전에 모든 Majority Write가 반영된 결과를 반환 |
| snapshot        | 특정 시점에 대한 결과를 반환(Point-In-Time Query)  |


![img.png](images/img-2.png)

* Read Concern Level 성능 및 안전성

### Causal Consistency

## Transaction

# MongoDB Index 기본 구조와 효율적인 탐색
