# -*- coding: utf-8 -*-
from odoo import models, fields, api

class address(models.Model):
    _name = 'batoi_logic.address'

    name = fields.Char(string='Street Address', size=60, required=True)
    customer_id = fields.Many2one('batoi_logic.customer', 'Customer', required=True)
    postal_code_id = fields.Many2one('batoi_logic.postal_code', 'Postal Code', required=True)
    orders_id = fields.One2many('batoi_logic.order', 'address_id', 'Associated orders')
    bills_id = fields.One2many('batoi_logic.bill', 'address_id', 'Bills with this address')

    # Weak entity
    _sql_constraints = [
            ('weak_entity_address_unique',
             'UNIQUE (customer_id, id)',
             'Weak entity address!')]
