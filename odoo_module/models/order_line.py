# -*- coding: utf-8 -*-
from openerp import models, fields, api

class order_Line(models.Model):
    _name = 'batoi_logic.order_line'

    quantity = fields.Integer(String='Quantity', required=True)
    order_id = fields.Many2one('batoi_logic.order', 'Order', required=True)
    product_id = fields.Many2one('batoi_logic.product', 'Product', required=True)

    # Weak entity
    _sql_constraints = [
        ('weak_entity_order_line_unique',
         'UNIQUE (order_id, id)',
         'Weak entity order line!')]
