package com.example.mongostudy

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation.group
import org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation
import org.springframework.data.mongodb.core.aggregation.Aggregation.previousOperation
import org.springframework.data.mongodb.core.aggregation.Aggregation.project
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.stereotype.Service

@Document(collection = "persons")
class Person(
    @Id
    val id: String? = null,
    val firstName: String,
    val lastName: String
)

interface PersonRepository : MongoRepository<Person, String>, QuerydslPredicateExecutor<Person>

@Service
class PersonQueryService(
    private val personRepository: PersonRepository,
    private val mongoTemplate: MongoTemplate
) {

    fun findBy(firstName: String): List<Person> {
        val query = Query.query(Criteria.where("firstName").isEqualTo(firstName))
        return mongoTemplate.find(query, Person::class.java)
    }

    fun groupByLastName(): List<LastNameGroup> {

        val aggregation = newAggregation(
            group("lastName"),
            project(LastNameGroup::class.java)
                .and(previousOperation()).`as`("lastName"),
        )

        val results =
            mongoTemplate.aggregate(
                aggregation,
                "persons",
                LastNameGroup::class.java
            )

        return results.mappedResults
    }

    fun asd(name: String): List<Person> {

        val person = QPerson.person

        return personRepository.findAll(person.firstName.eq(name)).toList()

    }
}

data class LastNameGroup(
    val lastName: String
)