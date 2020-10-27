# -*- coding: utf-8 -*-
from openerp import models, fields, api

class inventory(models.Model):
    _name = 'batoi_logic.inventory'

    stock = fields.Integer(String='Stock', required=True)
    location = fields.Text(String='Location', required=True)
    product_id = fields.Many2one('batoi_logic.product', 'product', required=True)

    _sql_constraints = [
        # One2one -->
        ('one2One_product', 'unique("product_id")',
         'Product must be unique, because is a One2one relationship!'),

        # Weak entity -->
        ('weak_entity_inventory_unique',
         'UNIQUE (product_id, id)',
         'Weak entity inventory!')
    ]
