# -*- coding: utf-8 -*-
{
    'name': "batoiLogic",

    'summary': """
        Manage your business with this module pretty simple and straightforward!""",

    'description': """
        This is my custom long description of this module, it is useful for managing a
        transport business like AMAZON but in a lower scale.
    """,

    'author': "Group 2",

    # Categories can be used to filter modules in modules listing
    # Check https://github.com/odoo/odoo/blob/13.0/odoo/addons/base/data/ir_module_category_data.xml
    # for the full list
    'category': 'Uncategorized',
    'version': '0.1',
    'application': True,

    # any module necessary for this one to work correctly
    'depends': ['base'],

    # always loaded
    'data': [
        'security/ir.model.access.csv',
        'views/customers.xml',
        'views/products.xml',
        'views/providers.xml',
        'views/addresses.xml',
        'views/cities.xml',
        'views/lorries.xml',
        'views/deliverymen.xml',
        'views/postalCodes.xml',
        'views/inventory.xml',
	'views/orders.xml'
    ],
}
