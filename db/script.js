/*
 * Author: Shri
 */

	use ticketing

	db.dropDatabase()

	db.createCollection('tickets')

    db.createCollection('csrs')

    db.csrs.insert({

        name: 'Yui Jensi',
        email: 'jui@redmart.com'
    })

    db.csrs.insert({
        name: 'Chun Tse',
        email: 'ctse@redmart.com'
    })

    db.createCollection('statuses')

    db.statuses.insert({

        name: 'New',
        code: NumberInt(1)
    })
    db.statuses.insert({

        name: 'Open',
        code: NumberInt(2)
    })
    db.statuses.insert({

        name: 'Closed',
        code: NumberInt(3)
    })

    db.createCollection('assignees')

    db.assignees.insert({
         name: 'Kevin Shaw',
         email: 'kshaw@redmart.com'
    })

    db.assignees.insert({
         name: 'Michael Bevan',
         email: 'mbevan@redmart.com'
    })

    db.createCollection('categories')


    db.categories.insert({
        name: 'Refund',
        desc: 'Refund for the item.'
    })

    db.categories.insert({
        name: 'LateDelivery',
        desc: 'Delivery arrived late'
    })
    db.categories.insert({
        name: 'DamagedGoods',
        desc: 'Item delivered is damaged'
    })
    db.categories.insert({
        name: 'OfferDispute',
        desc: 'Promised offer not availed'
    })
    db.categories.insert({
        name: 'TechnicalIssue',
        desc: 'Website or app related issue'
    })